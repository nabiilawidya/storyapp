package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.SignupResponse
import kotlinx.coroutines.launch

class SignupViewModel (private val repository: UserRepository) : ViewModel() {
    private val _signupResponse = MutableLiveData<SignupResponse>()
    val signupResponse: LiveData<SignupResponse> = _signupResponse

    fun signup(name: String, email: String, password: String){
        viewModelScope.launch {
            repository.signup(name, email, password)
        }
    }
}