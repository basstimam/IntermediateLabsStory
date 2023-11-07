package com.example.dicodingstoryapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingstoryapp.data.remote.ApiConfig
import com.example.dicodingstoryapp.data.remote.ListStoryItem
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _story = MutableLiveData<List<ListStoryItem>>()
    val story: LiveData<List<ListStoryItem>> = _story


    fun getAllStories(token: String) {
        viewModelScope.launch {
            try {
                val client = ApiConfig().getApiService(token)
                val response = client.getStories()

                if (response.error == false) {
                    // Filter out null elements and update the MutableLiveData
                    _story.value = response.listStory?.filterNotNull()
                } else {
                    // Handle API call failure
                    Log.e(TAG, "Failed to fetch stories: ${response.message.toString()}")
                }
            } catch (e: Exception) {
                // Handle other exceptions
                Log.e(TAG, "Exception occurred: ${e.message}")
            }
        }
    }


    companion object {
        const val TAG = "HomeViewModel"
    }
}
