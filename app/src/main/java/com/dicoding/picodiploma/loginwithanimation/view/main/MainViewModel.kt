package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.AuthRepository
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository, private val storyRepository: StoryRepository
) : ViewModel() {
    private val _userSession = MutableLiveData<UserModel>()
    private val _storyResponse = MutableLiveData<List<ListStoryItem>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val storyResponse: LiveData<List<ListStoryItem>> get() = _storyResponse

    init {
        loadUserSession()
    }

    private fun loadUserSession() {
        viewModelScope.launch {
            authRepository.getSession().collect { userModel ->
                _userSession.value = userModel
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return authRepository.getSession().asLiveData()
    }

    fun getStories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val listStory = storyRepository.getStories()
                _storyResponse.value = listStory
                _isLoading.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                _isLoading.postValue(false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}

