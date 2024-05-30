package com.dicoding.picodiploma.loginwithanimation.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.StoryApiService

class StoryPagingSource(private val storyApiService: StoryApiService) :
    PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = storyApiService.getStories(position, params.loadSize)

            val stories = responseData.listStory?.filterNotNull() ?: emptyList()

            LoadResult.Page(
                data = stories,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (stories.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}