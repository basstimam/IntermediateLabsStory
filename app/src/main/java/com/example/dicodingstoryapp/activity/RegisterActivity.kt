package com.example.dicodingstoryapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope

import com.example.dicodingstoryapp.data.remote.ApiConfig
import com.example.dicodingstoryapp.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener() {
            postRegister()
        }




    }


    private fun postRegister() {
        binding.apply {
           if (nameEditText.text.toString().isEmpty()) {
               nameEditText.error = "Nama tidak boleh kosong"
           }else {
               nameEditText.error = null
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



            Log.d("RegisterActivity", "Name : ${nameEditText.text}")
                Log.d("RegisterActivity", "Email : ${emailEditText.text}")
                Log.d("RegisterActivity", "Password : ${passwordEditText.text}")




                lifecycleScope.launch {
                    try {
                        val registerResponse = ApiConfig().getApiService().register(
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
                            Log.d("RegisterActivity", registerResponse.message.toString())
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registrasi gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registrasi gagal",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("RegisterActivity", "getRegisterState: ${e.message.toString()}")
                    }
                }

            }


        }
    }


