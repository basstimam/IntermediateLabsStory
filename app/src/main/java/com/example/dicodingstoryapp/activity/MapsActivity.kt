package com.example.dicodingstoryapp.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.dicodingstoryapp.R
import com.example.dicodingstoryapp.data.local.DataStorePref
import com.example.dicodingstoryapp.databinding.ActivityMapsBinding
import com.example.dicodingstoryapp.viewmodel.HomeViewModel
import com.example.dicodingstoryapp.viewmodel.ViewmodelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var dataStoreManager: DataStorePref
    private val boundsBuilder = LatLngBounds.Builder()

    private val viewModel: HomeViewModel by viewModels {
        ViewmodelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStoreManager = DataStorePref.getInstance(this@MapsActivity)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        initMapSettings()
        addMarker()


        lifecycleScope.launch {
            viewModel.getAllStories(dataStoreManager.readToken().toString())
        }
    }


    private fun initMapSettings() {
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }

    private fun addMarker() {
        viewModel.storyMap.observe(this) { list ->
            Log.d("MAPS", "onMapReady: $list")

            list.forEach { storyLocation ->
                val latLng = LatLng(storyLocation.lat!!, storyLocation.lon!!)
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title(storyLocation.name)
                    .snippet(storyLocation.description)

                mMap.addMarker(markerOptions)
                boundsBuilder.include(latLng)
            }

            val bounds = boundsBuilder.build()
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }


    }
}
