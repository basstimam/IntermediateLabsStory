package com.example.dicodingstoryapp.data.local

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.dicodingstoryapp.adapter.StoryPagingSource
import com.example.dicodingstoryapp.data.remote.ApiService
import com.example.dicodingstoryapp.data.remote.ListStoryItem

class StoryRepository(
    private val apiService: ApiService
) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

}