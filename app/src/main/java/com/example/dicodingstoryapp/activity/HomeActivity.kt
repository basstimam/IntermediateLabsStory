package com.example.dicodingstoryapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.dicodingstoryapp.R
import com.example.dicodingstoryapp.adapter.StoryAdapter
import com.example.dicodingstoryapp.data.local.DataStorePref
import com.example.dicodingstoryapp.data.remote.ApiConfig
import com.example.dicodingstoryapp.data.remote.ListStoryItem
import com.example.dicodingstoryapp.databinding.ActivityHomeBinding
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dataStoreManager: DataStorePref
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStorePref.getInstance(this@HomeActivity)

        binding.btnLogout.setOnClickListener {
            logout()
        }

        setupRecyclerView() // Set up RecyclerView

        lifecycleScope.launch {
            val token = dataStoreManager.readToken()
            val storyResponse = ApiConfig().getApiService(token.toString()).getStories()


            /*Warning*/
            if (storyResponse.error == false) {
                val stories = storyResponse.listStory as List<ListStoryItem>
                storyAdapter.submitList(stories) // Update the adapter with fetched data
                Log.d("HomeActivity", "Stories: " + stories.toString())
            } else {
                // Handle API call failure
                Log.d("HomeActivity", "Failed to fetch stories")
            }
        }
    }

    private fun setupRecyclerView() {
        storyAdapter = StoryAdapter()

        binding.rvStory.apply {
            adapter = storyAdapter
            setHasFixedSize(true)
            // Set layout manager if needed (e.g., LinearLayoutManager)
            // layoutManager = LinearLayoutManager(context)
        }
    }

    private fun logout() {
        lifecycleScope.launch {
            dataStoreManager.deleteToken()
            finish()
        }
    }
}
