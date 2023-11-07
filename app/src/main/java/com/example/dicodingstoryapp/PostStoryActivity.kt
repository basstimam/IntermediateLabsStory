package com.example.dicodingstoryapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dicodingstoryapp.databinding.ActivityPostStoryBinding

class PostStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}