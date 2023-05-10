package com.dicoding.storyapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.storyapp.api.ApiService
import com.dicoding.storyapp.api.StoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileInputStream
import java.util.*

class StoryPagingSource(private val apiService: ApiService): PagingSource<Int, StoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, StoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val properties = Properties()
            val inputStream = withContext(Dispatchers.IO) {
                FileInputStream("local.properties")
            }
            withContext(Dispatchers.IO) {
                properties.load(inputStream)
            }
            val defaultApiKey = properties.getProperty("API_KEY")
            val responseData = apiService.getStories("Bearer $defaultApiKey", position, params.loadSize)
            LoadResult.Page(
                data = responseData,
                prevKey = if(position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if(responseData.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}