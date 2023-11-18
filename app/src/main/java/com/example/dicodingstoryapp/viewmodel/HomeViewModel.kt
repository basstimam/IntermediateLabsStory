package com.example.dicodingstoryapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.dicodingstoryapp.data.local.StoryRepository
import com.example.dicodingstoryapp.data.remote.ApiConfig
import com.example.dicodingstoryapp.data.remote.ListStoryItem
import kotlinx.coroutines.launch

class HomeViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _story = MutableLiveData<List<ListStoryItem>>()
    val story: LiveData<List<ListStoryItem>> = _story

    val pagingStory: LiveData<PagingData<ListStoryItem>> = storyRepository.getStory().cachedIn(viewModelScope)


    fun getAllStories(token: String) {
        viewModelScope.launch {
            try {
                val client = ApiConfig().getApiService(token)
                val response = client.getStories(1,5)

                if (response.error == false) {

                    _story.value = response.listStory?.filterNotNull()
                } else {

                    Log.e(TAG, "Failed to fetch stories: ${response.message.toString()}")
                }
            } catch (e: Exception) {

                Log.e(TAG, "Exception occurred: ${e.message}")
            }
        }
    }


    companion object {
        const val TAG = "HomeViewModel"
    }
}
