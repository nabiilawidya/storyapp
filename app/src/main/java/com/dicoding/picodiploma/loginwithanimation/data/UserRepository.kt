package com.dicoding.picodiploma.loginwithanimation.data


import androidx.lifecycle.asLiveData
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val userPreference: UserPreference, private val apiService: ApiService
) {
    suspend fun signup(name: String, email: String, password: String) =
        apiService.register(name, email, password)

    suspend fun login(email: String, password: String) = apiService.login(email, password)

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    suspend fun getStories(): List<ListStoryItem> {
        val response = apiService.getStories()
        return if (response.error == false) {
            response.listStory?.filterNotNull() ?: emptyList()
        } else {
            throw Exception("Error fetching stories: ${response.message}")
        }
    }

    suspend fun addStory(
        multipartBody: MultipartBody.Part,
        description: RequestBody,
        lat: Float? = null,
        lon: Float? = null
    ) = apiService.uploadImage(multipartBody, description, lat, lon)

    fun getUser() = userPreference.getSession().asLiveData()

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference, apiService: ApiService
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userPreference, apiService)
        }.also { instance = it }
    }
}
