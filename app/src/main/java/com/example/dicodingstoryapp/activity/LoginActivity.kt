package com.example.dicodingstoryapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.doOnTextChanged
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

       binding.apply {
                passwordEditText.doOnTextChanged { _, _, _, _ -> validator() }

              emailEditText.doOnTextChanged { _, _, _, _ -> validator() }

              loginButton.setOnClickListener {
                postLogin()
              }
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

    private fun showLoading(state: Boolean)
    { binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE }

    private fun validator() {
        var isValid = true

        binding.apply {
            if (emailEditText.error != null || emailEditText.text.toString().isEmpty()) isValid = false
            if (passwordEditText.error != null || passwordEditText.text.toString().isEmpty()) isValid = false

            loginButton.isEnabled = isValid
        }
    }


}
