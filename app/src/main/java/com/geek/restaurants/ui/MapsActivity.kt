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
import java.util.*

class MapsActivity : AppCompatActivity() {

    private  var latitude: Double = 0.0
    private  var longitude: Double =0.0
    private var restName: String? = ""
    private var street: String? = ""
    private var borough: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

         latitude = intent.extras!!.getDouble("LATITUDE")
         longitude = intent.extras!!.getDouble("LONGITUDE")
        restName = intent.extras!!.getString("NAME")
        street = intent.extras!!.getString("STREET")
        borough = intent.extras!!.getString("BOROUGH")



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync{ googleMap ->
            addMarkers(googleMap)
        }
    }

    private fun addMarkers(googleMap: GoogleMap) {

        val position = LatLng(longitude, latitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f))
        val snippet = String.format(
            Locale.getDefault(),
            "Street: %1s, Name: %2s",
            street,
            restName
        )

        googleMap.addMarker(MarkerOptions().position(position).snippet(snippet).title(borough))



    }
}