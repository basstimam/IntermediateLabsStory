package com.example.dicodingstoryapp

import android.content.Context
import com.example.dicodingstoryapp.data.local.DataStorePref
import com.example.dicodingstoryapp.data.local.StoryRepository
import com.example.dicodingstoryapp.data.remote.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val dataStore = DataStorePref.getInstance(context)
        val token = dataStore.readToken()
        val apiService = ApiConfig().getApiService(token)

        return StoryRepository(apiService)

    }
}



