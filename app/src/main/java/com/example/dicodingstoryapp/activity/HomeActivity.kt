package com.example.dicodingstoryapp.activity

import StoryAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingstoryapp.R
import com.example.dicodingstoryapp.adapter.LoadingStateAdapter
import com.example.dicodingstoryapp.data.local.DataStorePref
import com.example.dicodingstoryapp.databinding.ActivityHomeBinding

import com.example.dicodingstoryapp.viewmodel.HomeViewModel
import com.example.dicodingstoryapp.viewmodel.ViewmodelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dataStoreManager: DataStorePref
    private val homeViewModel: HomeViewModel by viewModels {
        ViewmodelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        dataStoreManager = DataStorePref.getInstance(this@HomeActivity)


binding.apply {
    logoutFab.setOnClickListener {
        logoutDialog()
    }

    postStoryFab.setOnClickListener{
        val intent = Intent(this@HomeActivity, PostStoryActivity::class.java)
        startActivity(intent)
    }
}



lifecycleScope.launch {
    homeViewModel.getAllStories(dataStoreManager.readToken().toString())
}


        homeViewModel.pagingStory.observe(this) { story ->

            val adapter = StoryAdapter()
            adapter.submitData(lifecycle, story)
            binding.rvStory.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter.LoadingStateAdapter {
                    adapter.retry()
                }
            )
        }

        val layoutManager = ZoomRecyclerLayout(this)
        binding.rvStory.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

    }


    private fun logoutDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.logoutDialogTitle))
            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)

                logout()
                finish()
            }
            .show()
    }


    private fun logout() {
        lifecycleScope.launch {
            dataStoreManager.deleteToken()
            Toast.makeText(this@HomeActivity, "Logout Success", Toast.LENGTH_SHORT).show()
            finish()
        }
    }


}
