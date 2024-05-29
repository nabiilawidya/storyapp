package com.dicoding.picodiploma.loginwithanimation.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.loginwithanimation.data.AuthRepository
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.di.Injection
import com.dicoding.picodiploma.loginwithanimation.view.addstory.AddStoryViewModel
import com.dicoding.picodiploma.loginwithanimation.view.login.LoginViewModel
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel
import com.dicoding.picodiploma.loginwithanimation.view.maps.MapsViewModel
import com.dicoding.picodiploma.loginwithanimation.view.signup.SignupViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository, private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(authRepository, storyRepository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyRepository, authRepository) as T
            }

            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyRepository, authRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            val authRepository = Injection.provideAuthRepository(context)
            val storyRepository = Injection.provideStoryRepository(context)
            return ViewModelFactory(authRepository, storyRepository)
        }
    }
}
