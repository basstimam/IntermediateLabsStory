package com.example.dicodingstoryapp.activity

import StoryAdapter
import android.content.Intent
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        supportActionBar?.hide()

        binding.logoutFab.setOnClickListener {

            MaterialAlertDialogBuilder(this)
                .setTitle(resources.getString(R.string.logoutDialogTitle))

                .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->


                }
                .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    logout()


                }
                .show()

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

        binding.rvStory.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

    }



    private fun logout() {
        lifecycleScope.launch {
            dataStoreManager.deleteToken()
            Toast.makeText(this@HomeActivity, "Logout Success", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
