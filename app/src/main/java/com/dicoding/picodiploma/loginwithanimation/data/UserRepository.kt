package com.dicoding.picodiploma.loginwithanimation.data


import android.util.Log
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun signup(name: String, email: String, password: String) =
        apiService.register(name, email, password)

    suspend fun login(email: String, password: String) =
        apiService.login(email, password)

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun getStories(): List<ListStoryItem> {
        val token = userPreference.getToken().first()
        Log.d("UserRepository", "Token retrieved: $token")  // Log token
        if (token.isEmpty()) throw Exception("Missing authentication token")

        val response = apiService.getStories("Bearer $token")
        return if (response.error == false) {
            response.listStory?.filterNotNull() ?: emptyList()
        } else {
            throw Exception("Error fetching stories: ${response.message}")
        }
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun getToken(): Flow<String> {
        return userPreference.getToken()
    }

    suspend fun clearToken() {
        userPreference.clearToken()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}
