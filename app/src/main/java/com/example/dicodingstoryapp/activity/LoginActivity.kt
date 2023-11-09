package com.example.dicodingstoryapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.example.dicodingstoryapp.R
import com.example.dicodingstoryapp.data.local.DataStorePref

import com.example.dicodingstoryapp.data.remote.ApiConfig
import com.example.dicodingstoryapp.databinding.ActivityLoginBinding

import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dataStoreManager: DataStorePref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStorePref.getInstance(this@LoginActivity)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.loginButton.setOnClickListener {
            postLogin()
        }


    }

    @SuppressLint("SuspiciousIndentation")
    private fun postLogin() {
        showLoading(true)

        binding.apply {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()) {
                emailEditTextLayout.error = "Email tidak valid"
                showLoading(false)
                return
            }


        }

        lifecycleScope.launch {
            try {
                val loginResponse = ApiConfig().getApiService(null).login(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )


                    dataStoreManager.saveToken(loginResponse.loginResult?.token.toString())
                Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show(
                )
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()



            } catch (e: Exception) {
                showLoading(false)
                Log.e("LoginActivity", "Error: ${e.message}")
                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = android.view.View.VISIBLE
        } else {
            binding.progressBar.visibility = android.view.View.GONE
        }
    }


}
