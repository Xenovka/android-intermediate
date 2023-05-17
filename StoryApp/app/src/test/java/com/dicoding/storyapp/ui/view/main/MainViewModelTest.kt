package com.dicoding.storyapp.ui.view.main

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.test.core.app.ApplicationProvider
import com.dicoding.storyapp.api.ApiConfig
import com.dicoding.storyapp.api.ApiService
import com.dicoding.storyapp.api.StoryItem
import com.dicoding.storyapp.DummyData
import com.dicoding.storyapp.MainDispatcherRule
import com.dicoding.storyapp.adapter.StoryListAdapter
import com.dicoding.storyapp.data.StoryRepository
import com.dicoding.storyapp.getOrAwaitValue
import com.dicoding.storyapp.model.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

private const val TEST_DATASTORE: String = "TEST_DATASTORE"

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Mock
    private lateinit var mainViewModel: MainViewModel
    private lateinit var pref: UserPreference
    private lateinit var storyRepository: StoryRepository
    private lateinit var apiService: ApiService
    private val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUFmMDNiWkloMi1sbW4ycW8iLCJpYXQiOjE2ODIxNzA5NTV9.0aMjCtcw6B7sNppymxhtxUE84YPuVQT3WPUohgd5ZTI"

    @Before
    fun setup() {
        apiService = ApiConfig.getApiService()
        pref = UserPreference.getInstance(dataStore = PreferenceDataStoreFactory.create {
            ApplicationProvider.getApplicationContext<Context>().preferencesDataStoreFile(
                TEST_DATASTORE
            )
        })
        storyRepository = Mockito.mock(StoryRepository::class.java)
    }

    @Test
    fun `when GET allStories Should Not Null and Return Data`() = runTest {
        val dummyStory = DummyData.generateDummyData()
        val data: PagingData<StoryItem> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoryItem>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories(token)).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(pref, storyRepository)
        val actualStory: PagingData<StoryItem> = mainViewModel.story(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when GET allStories Empty Shound Return No Data`() = runTest {
        val data: PagingData<StoryItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<StoryItem>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories(token)).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(pref, storyRepository)
        val actualStory: PagingData<StoryItem> = mainViewModel.story(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )
        differ.submitData(actualStory)

        assertEquals(0, differ.snapshot().size)
    }

    class StoryPagingSource : PagingSource<Int, LiveData<List<StoryItem>>>() {
        companion object {
            fun snapshot(items: List<StoryItem>): PagingData<StoryItem> {
                return PagingData.from(items)
            }
        }
        override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryItem>>>): Int {
            return 0
        }
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryItem>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }

    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}