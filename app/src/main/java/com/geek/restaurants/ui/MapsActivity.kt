package com.geek.restaurants.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.geek.restaurants.R
import com.geek.restaurants.databinding.ActivityMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import timber.log.Timber

class MapsActivity : AppCompatActivity() {

    private lateinit var mMap: GoogleMap
    private  var latitude: Double = 0.0
    private  var longitude: Double =0.0
    private var restName: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

         latitude = intent.extras!!.getDouble("LATITUDE")
         longitude = intent.extras!!.getDouble("LONGITUDE")
        restName = intent.extras!!.getString("NAME")


        Timber.d("Latitude received %s", latitude)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync{ googleMap ->
            addMarkers(googleMap)
        }
    }

    private fun addMarkers(googleMap: GoogleMap) {

        val position = LatLng(latitude, longitude)
        googleMap.addMarker(MarkerOptions().position(position).title(restName))

       // googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))


    }
}