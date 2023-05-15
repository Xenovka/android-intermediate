package com.dicoding.storyapp.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.dicoding.storyapp.api.ApiService
import com.dicoding.storyapp.api.StoryItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class StoryPagingSourceTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: ApiService
    private lateinit var storyPagingSource: StoryPagingSource
    private val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUFmMDNiWkloMi1sbW4ycW8iLCJpYXQiOjE2ODIxNzA5NTV9.0aMjCtcw6B7sNppymxhtxUE84YPuVQT3WPUohgd5ZTI"

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        storyPagingSource = StoryPagingSource(apiService, token)
    }

    @Test
    fun `paging source load - failure`() = runTest {
        given(apiService.getStories(token)).willReturn(null)

        val expectedResult = PagingSource.LoadResult.Error<Int, StoryItem>(
            java.lang.NullPointerException()
        )

        assertEquals(
            expectedResult.toString(), storyPagingSource.load(
                PagingSource.LoadParams.Refresh(
                    key = 0,
                    loadSize = 1,
                    placeholdersEnabled = false
                )
            ).toString()
        )
    }
}