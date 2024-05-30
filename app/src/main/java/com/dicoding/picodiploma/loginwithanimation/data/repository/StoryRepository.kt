package com.dicoding.picodiploma.loginwithanimation.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.data.StoryPagingSource
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.StoryApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val storyApiService: StoryApiService
) {
    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(config = PagingConfig(
            pageSize = 5
        ), pagingSourceFactory = {
            StoryPagingSource(storyApiService)
        }).liveData
    }

    suspend fun addStory(
        multipartBody: MultipartBody.Part,
        description: RequestBody,
        lat: Float? = null,
        lon: Float? = null
    ) = storyApiService.uploadImage(multipartBody, description, lat, lon)

    suspend fun getStoriesWithLocation() = storyApiService.getStoriesWithLocation()

    companion object {
        fun getInstance(storyApiService: StoryApiService): StoryRepository {
            return StoryRepository(storyApiService)
        }
    }
}