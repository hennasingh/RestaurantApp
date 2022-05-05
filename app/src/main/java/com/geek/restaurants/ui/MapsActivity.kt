package com.geek.restaurants.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.geek.restaurants.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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

    @SuppressLint("PotentialBehaviorOverride")
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


//        googleMap.setOnMarkerClickListener(OnMarkerClickListener { marker -> // on marker click we are getting the title of our marker
//                //Call the function to send data to the friend
//
//
//            false
//        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.friends, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.friends -> {
               val intent = Intent(this, FriendsActivity::class.java)
                //save map data in Intent instead of calling click listener on Map

                intent.putExtra("LATITUDE", latitude)
                intent.putExtra("LONGITUDE", longitude)
                intent.putExtra("NAME", restName)
                intent.putExtra("STREET", street)
                intent.putExtra("BOROUGH", borough)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}