package com.dicoding.picodiploma.loginwithanimation.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel(
    private val repository: StoryRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _storiesWithLocationResponse = MutableLiveData<StoryResponse>()
    val storiesWithLocationResponse: LiveData<StoryResponse> = _storiesWithLocationResponse

    fun getStoriesWithLocation() {
        viewModelScope.launch {
            try {
                val response = repository.getStoriesWithLocation()
                _storiesWithLocationResponse.value = response
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
                val errorMessage = errorBody.message
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }

    fun getUser(): LiveData<UserModel> = authRepository.getUser()

    companion object {
        private const val TAG = "MapsViewModel"
    }
}