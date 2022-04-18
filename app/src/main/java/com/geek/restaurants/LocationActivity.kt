package com.geek.restaurants

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.realm.mongodb.User
import kotlinx.android.synthetic.main.activity_location.*
import org.bson.Document
import timber.log.Timber

class LocationActivity : AppCompatActivity() {

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        
        btn_manhattan.setOnClickListener{
            openPartition("Manhattan")
        }
        
        btn_brooklyn.setOnClickListener{
            openPartition("Brooklyn")
        }
    }

    private fun openPartition(partition: String) {

        val intent = Intent(this, RestaurantListActivity::class.java)
        intent.putExtra("EXTRA_PARTITION",partition!!)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()

        user = restaurantApp.currentUser()
        Timber.d("User is $user")
        if(user == null){
            // if no user is currently logged in, start the login activity so the user can authenticate
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


}