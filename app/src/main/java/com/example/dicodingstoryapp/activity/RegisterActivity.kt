package com.example.dicodingstoryapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope

import com.example.dicodingstoryapp.data.remote.ApiConfig
import com.example.dicodingstoryapp.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch
import okhttp3.ResponseBody.Companion.toResponseBody
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.registerButton.setOnClickListener {
            postRegister()
        }


    }


    private fun postRegister() {

        showLoading(true)
        binding.apply {

            if (nameEditText.text.toString().isEmpty()) {
                nameEditTextLayout.error = "Nama tidak boleh kosong"
            } else {
                nameEditTextLayout.error = null
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString())
                    .matches()
            ) {
                emailInputLayout.error = "Email tidak valid"
            } else {
                emailInputLayout.error = null
            }

            if (passwordEditText.text?.length!! < 8) {
                passwordEditTextLayout.error = "Password minimal 8 karakter"
            } else {
                passwordEditTextLayout.error = null
            }

            if (nameEditText.text.toString().isNotEmpty() &&
                android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString())
                    .matches() &&
                passwordEditText.text?.length!! >= 8
            ) {
                lifecycleScope.launch {
                    try {
                        val registerResponse = ApiConfig().getApiService(null).register(
                            nameEditText.text.toString(),
                            emailEditText.text.toString(),
                            passwordEditText.text.toString()
                        )


                        registerResponse.message?.let { Log.d("RegisterActivity", it) }

                        if (registerResponse.error == false) {

                          Toast.makeText(
                                this@RegisterActivity,
                                "Registrasi berhasil",
                                Toast.LENGTH_SHORT
                            ).show()


                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            showLoading(false)
                            registerResponse.message?.let { message ->
                                Toast.makeText(
                                    this@RegisterActivity,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        showLoading(false)

                        Log.e("RegisterActivity", "Error: ${e.message?.toResponseBody()}")
                        Toast.makeText(
                            this@RegisterActivity,
                            "Email telah digunakan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
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



