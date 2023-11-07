package com.example.dicodingstoryapp.activity

import StoryAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingstoryapp.R

import com.example.dicodingstoryapp.data.local.DataStorePref
import com.example.dicodingstoryapp.data.remote.ApiConfig
import com.example.dicodingstoryapp.data.remote.ListStoryItem
import com.example.dicodingstoryapp.databinding.ActivityHomeBinding
import com.example.dicodingstoryapp.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dataStoreManager: DataStorePref
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStorePref.getInstance(this@HomeActivity)

        binding.btnLogout.setOnClickListener {
            logout()
        }
lifecycleScope.launch {
    homeViewModel.getAllStories(dataStoreManager.readToken().toString())
}


        homeViewModel.story.observe(this) { story ->

            val adapter = StoryAdapter()
            adapter.submitList(story)
            binding.rvStory.adapter = adapter
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)
    }



    private fun logout() {
        lifecycleScope.launch {
            dataStoreManager.deleteToken()
            finish()
        }
    }
}
