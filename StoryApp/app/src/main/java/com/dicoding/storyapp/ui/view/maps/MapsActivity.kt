package com.dicoding.storyapp.ui.view.maps

import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.R
import com.dicoding.storyapp.api.StoryItem
import com.dicoding.storyapp.databinding.ActivityMapsBinding
import com.dicoding.storyapp.model.UserPreference
import com.dicoding.storyapp.ui.view.ViewModelFactory

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        viewModel.getStories().observe(this) { stories ->
            for(story in stories) {
                Log.d("StoriesResult:", story.toString())
            }
        }

        setMapStyle()
        viewModel.getStories().observe(this) {
            addManyMarkers(it)
        }

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MapsViewModel::class.java]

        viewModel.setStories()
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if(!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun addManyMarkers(data: ArrayList<StoryItem>) {
        val stories = data.toList()
        stories.forEach { story ->
            if(story.lat != null && story.lon != null) {
                val latLng = LatLng(story.lat.toDouble(), story.lon.toDouble())
                mMap.addMarker(MarkerOptions().position(latLng).title(story.name))
            }

        }
    }

    companion object {
        private const val TAG = "MapsActivity"
    }
}