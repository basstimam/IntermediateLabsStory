package com.example.dicodingstoryapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.example.dicodingstoryapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        val receivedBundle = intent.extras


        binding.apply {
            detailImageStory.let {
                Glide.with(this@DetailStoryActivity)
                    .load(receivedBundle?.getString("IMAGE"))
                    .into(it)
            }


            detailTitleStory.text = receivedBundle?.getString("NAME")
            detailDescriptionStory.text = receivedBundle?.getString("DESCRIPTION")
            detailDateStory.text = receivedBundle?.getString("CREATEDAT")
            detailLatitudeStory.text = "Latitude: " + receivedBundle?.getString("LAT")
            detailLongitudeStory.text = "Longitude: " + receivedBundle?.getString("LON")

        }



    }



}