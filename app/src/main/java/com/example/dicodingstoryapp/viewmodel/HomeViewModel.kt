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

    private val _storyMap = MutableLiveData<List<ListStoryItem>>()
    val storyMap: LiveData<List<ListStoryItem>> = _storyMap

    val pagingStory: LiveData<PagingData<ListStoryItem>> = storyRepository.getStory().cachedIn(viewModelScope)




    fun getAllStories(token: String) {
        viewModelScope.launch {
            try {
                val client = ApiConfig().getApiService(token)
                val response = client.getStories(1, 5)
                val storyMapResponse = client.getStoriesWithLocation(location = 1, size = 50)

                if (response.error == false) {
                    _story.value = response.listStory?.filterNotNull()
                    Log.d(TAG, "Success to fetch stories: ${response.listStory?.toString()}")
                } else {
                    Log.e(TAG, "Failed to fetch stories: ${response?.message?.toString()}")
                }

                if (storyMapResponse.error == false) {
                    _storyMap.value = storyMapResponse.listStory?.filterNotNull()
                    Log.d(TAG, "Success to fetch stories with location: ${storyMapResponse.listStory?.toString()}")
                } else {
                    Log.e(TAG, "Failed to fetch stories with location: ${storyMapResponse?.message?.toString()}")
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
