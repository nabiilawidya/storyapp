package com.dicoding.picodiploma.loginwithanimation.data.repository

import androidx.lifecycle.asLiveData
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.AuthApiService
import kotlinx.coroutines.flow.Flow

class AuthRepository private constructor(
    private val userPreference: UserPreference, private val authApiService: AuthApiService
) {
    suspend fun signup(name: String, email: String, password: String) =
        authApiService.register(name, email, password)

    suspend fun login(email: String, password: String) = authApiService.login(email, password)

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun getUser() = userPreference.getSession().asLiveData()

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            userPreference: UserPreference, authApiService: AuthApiService
        ): AuthRepository = instance ?: synchronized(this) {
            instance ?: AuthRepository(userPreference, authApiService)
        }.also { instance = it }
    }
}