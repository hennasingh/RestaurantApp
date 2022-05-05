package com.geek.restaurants.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.geek.restaurants.R

class FriendsActivity : AppCompatActivity() {

    private  var latitude: Double = 0.0
    private  var longitude: Double =0.0
    private var restName: String? = ""
    private var street: String? = ""
    private var borough: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        latitude = intent.extras!!.getDouble("LATITUDE")
        longitude = intent.extras!!.getDouble("LONGITUDE")
        restName = intent.extras!!.getString("NAME")
        street = intent.extras!!.getString("STREET")
        borough = intent.extras!!.getString("BOROUGH")

        //Add a float button to add a friend, and then share the above information with the person

        //To do - how will the receiver get info of the location.
    }
}