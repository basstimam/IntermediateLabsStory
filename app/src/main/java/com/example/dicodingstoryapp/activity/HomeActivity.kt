package com.example.dicodingstoryapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.dicodingstoryapp.R
import com.example.dicodingstoryapp.adapter.StoryAdapter
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

           Log.d("HomeActivity", "Story: " + story.toString())
            Toast.makeText(this, story.toString(), Toast.LENGTH_SHORT).show()

        }
    }



    private fun logout() {
        lifecycleScope.launch {
            dataStoreManager.deleteToken()
            finish()
        }
    }
}
