package com.example.dicodingstoryapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.dicodingstoryapp.data.local.DataStorePref
import com.example.dicodingstoryapp.data.remote.ApiConfig
import com.example.dicodingstoryapp.data.remote.FileUploadResponse
import com.example.dicodingstoryapp.databinding.ActivityPostStoryBinding
import com.example.dicodingstoryapp.getImageUri
import com.example.dicodingstoryapp.reduceFileImage
import com.example.dicodingstoryapp.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class PostStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostStoryBinding
    private var currentImageUri: Uri? = null
    private lateinit var dataStoreManager: DataStorePref


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStoreManager = DataStorePref.getInstance(this@PostStoryActivity)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.apply {
            galeryFab.setOnClickListener {
                startGallery()

            }

            cameraFab.setOnClickListener {
                startCamera()
            }

            uploadStoryButton.setOnClickListener {
                uploadImage()
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
            val imageFile = uriToFile(it, this)

            if (imageFile.length() > 10485760) {
                Toast.makeText(this, "Ukuran gambar terlalu besar", Toast.LENGTH_SHORT).show()
                return
            }
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

    private fun uploadImage() {
        if (binding.descTextEdit.text.toString().isEmpty()) {
            binding.descTextEdit.error = "Deskripsi tidak boleh kosong"
            return
        }

        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.descTextEdit.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )




            lifecycleScope.launch {
                try {

                    val apiService = ApiConfig().getApiService(dataStoreManager.readToken())
                    val successResponse = apiService.uploadImage(multipartBody, description)

                    Toast.makeText(this@PostStoryActivity, successResponse.message, Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@PostStoryActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                   Toast.makeText(this@PostStoryActivity, errorResponse.message, Toast.LENGTH_SHORT).show()
                }
            }
        } ?: Toast.makeText(this, "Gambar tidak ditemukan", Toast.LENGTH_SHORT).show()
    }
}