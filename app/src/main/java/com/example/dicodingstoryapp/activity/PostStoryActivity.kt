package com.example.dicodingstoryapp.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.dicodingstoryapp.databinding.ActivityPostStoryBinding
import com.example.dicodingstoryapp.getImageUri

class PostStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostStoryBinding
    private var currentImageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            galeryFab.setOnClickListener {
                startGallery()

            }

            cameraFab.setOnClickListener {
                startCamera()
            }
        }
    }






    private fun startGallery(){
        launcherGallery.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }


    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage(){
        currentImageUri?.let{
            Log.d("Photo Picker", "Uri: $it")
            binding.previewImageView.setImageURI(it)
        }
    }


    private fun startCamera(){
        currentImageUri = getImageUri(this)
launcherIntentCamera.launch(currentImageUri)

    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }
}