package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository, private val storyRepository: StoryRepository
) : ViewModel() {
    private val _userSession = MutableLiveData<UserModel>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

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

    val stories: LiveData<PagingData<ListStoryItem>> by lazy {
        storyRepository.getStories().cachedIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}

