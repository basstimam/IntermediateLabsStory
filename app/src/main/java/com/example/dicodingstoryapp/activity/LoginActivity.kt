package com.example.dicodingstoryapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        }

        lifecycleScope.launch {
            val loginResponse = ApiConfig().getApiService(null).login(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )

            if(loginResponse.error == false) {
               MotionToast.createColorToast(
                    this@LoginActivity,
                    "Login Success",
                    "Welcome to Story App",
                    MotionToastStyle.SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@LoginActivity, www.sanju.motiontoast.R.font.helvetica_regular)
               )


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