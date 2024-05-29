package com.dicoding.picodiploma.loginwithanimation.view.maps

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.dicoding.picodiploma.loginwithanimation.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMapsBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()
    private val mapsViewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mapsViewModel.getUser().observe(this) {
            mapsViewModel.getStoriesWithLocation()
        }

        setMapStyle()
        addManyMarker()
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun addManyMarker() {
        mapsViewModel.storiesWithLocationResponse.observe(this) { response ->
            response?.listStory?.forEach { story ->
                story?.let {
                    val lat = it.lat
                    val lon = it.lon
                    if (lat != null && lon != null) {
                        val latLng = LatLng(lat, lon)
                        mMap.addMarker(
                            MarkerOptions().position(latLng).title(it.name).snippet(it.description)
                        )
                        boundsBuilder.include(latLng)
                    }
                }
            }


            val bounds: LatLngBounds = boundsBuilder.build()
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    300
                )
            )
        }
    }

    companion object {
        private val TAG = MapsActivity::class.java.simpleName
    }
}