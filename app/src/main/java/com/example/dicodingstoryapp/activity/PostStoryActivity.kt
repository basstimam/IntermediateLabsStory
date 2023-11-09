package com.example.dicodingstoryapp.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.example.dicodingstoryapp.data.local.DataStorePref
import com.example.dicodingstoryapp.data.remote.ApiConfig
import com.example.dicodingstoryapp.data.remote.FileUploadResponse
import com.example.dicodingstoryapp.databinding.ActivityPostStoryBinding
import com.example.dicodingstoryapp.getImageUri
import com.example.dicodingstoryapp.reduceFileImage
import com.example.dicodingstoryapp.uriToFile
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.File
import kotlin.math.log

class PostStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostStoryBinding
    private var currentImageUri: Uri? = null
    private lateinit var dataStoreManager: DataStorePref


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStoreManager = DataStorePref.getInstance(this@PostStoryActivity)

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

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.descTextEdit.text.toString().toRequestBody("text/plain".toMediaType())

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            lifecycleScope.launch {
                try {

                    val apiService = ApiConfig().getApiService(dataStoreManager.readToken())
                    val successResponse = apiService.uploadImage(multipartBody, description)

                    MotionToast.createColorToast(
                        this@PostStoryActivity,
                        "Success",
                        successResponse.message,
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this@PostStoryActivity, www.sanju.motiontoast.R.font.helvetica_regular)
                    )

                    val intent = Intent(this@PostStoryActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                   MotionToast.createColorToast(
                            this@PostStoryActivity,
                            "Error",
                            errorResponse.message,
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@PostStoryActivity, www.sanju.motiontoast.R.font.helvetica_regular)
                        )
                }
            }
        } ?: Toast.makeText(this, "Gambar tidak ditemukan", Toast.LENGTH_SHORT).show()
    }
}