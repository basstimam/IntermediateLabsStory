package com.example.dicodingstoryapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.dicodingstoryapp.R
import com.example.dicodingstoryapp.data.local.DataStorePref
import com.example.dicodingstoryapp.databinding.ActivityHomeBinding
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dataStoreManager: DataStorePref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStorePref.getInstance(this@HomeActivity)


        binding.btnLogout.setOnClickListener(){
            logout()
        }
    }


    private fun logout(){
        lifecycleScope.launch{
            dataStoreManager.deleteToken()
            finish()
        }

    }
}