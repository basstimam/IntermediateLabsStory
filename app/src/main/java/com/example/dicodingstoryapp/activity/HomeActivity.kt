package com.example.dicodingstoryapp.activity

import StoryAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingstoryapp.R

import com.example.dicodingstoryapp.data.local.DataStorePref
import com.example.dicodingstoryapp.data.remote.ApiConfig
import com.example.dicodingstoryapp.data.remote.ListStoryItem
import com.example.dicodingstoryapp.databinding.ActivityHomeBinding
import com.example.dicodingstoryapp.viewmodel.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var dataStoreManager: DataStorePref
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


        homeViewModel.story.observe(this) { story ->

            val adapter = StoryAdapter()
            adapter.submitList(story)
            binding.rvStory.adapter = adapter
        }

        val layoutManager = ZoomRecyclerLayout(this)
        binding.rvStory.layoutManager = layoutManager
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL


        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)




    }


    private fun logoutDialog(){
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.logoutDialogTitle))

            .setNeutralButton(resources.getString(R.string.cancel)) { dialog, which ->
                dialog.dismiss()


            }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                logout()

            }
            .show()
    }


    private fun logout() {
        lifecycleScope.launch {
            dataStoreManager.deleteToken()
            MotionToast.createColorToast(
                this@HomeActivity,
                "Logout Success",
                "Thank you for using Dicoding Story App",
                MotionToastStyle.INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this@HomeActivity, www.sanju.motiontoast.R.font.helvetica_regular)
            )
            finish()
        }
    }


}
