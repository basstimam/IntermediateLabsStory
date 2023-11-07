package com.example.dicodingstoryapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.dicodingstoryapp.data.local.DataStorePref

import com.example.dicodingstoryapp.data.remote.ApiConfig
import com.example.dicodingstoryapp.databinding.ActivityLoginBinding

import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var dataStoreManager: DataStorePref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStorePref.getInstance(this@LoginActivity)


        binding.loginButton.setOnClickListener() {

            postLogin()

        }



       lifecycleScope.launch {
              val token = dataStoreManager.readToken()
              Log.d("LoginActivity", "Token: " + token.toString())
       }








    }


    private fun postLogin(){
        binding.apply {
            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString())
                    .matches()
            ) {
                emailEditTextLayout.error = "Email tidak valid"
            } else {
                emailEditTextLayout.error = null
            }

            if (passwordEditText.text?.length!! < 8) {
                passwordEditTextLayout.error = "Password minimal 8 karakter"
            } else {
                passwordEditTextLayout.error = null
            }

            Log.d("LoginActivity", "Email : ${emailEditText.text}")
            Log.d("LoginActivity", "Password : ${passwordEditText.text}")
        }

        lifecycleScope.launch {
            val loginResponse = ApiConfig().getApiService(null).login(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )

            if(loginResponse.error == false) {
                Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()


                    dataStoreManager.saveToken(loginResponse.loginResult?.token.toString())
                    Log.d("LoginActivity", "Token: " + dataStoreManager.readToken().toString())
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()


            } else {
                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()

            }





        }


    }






}