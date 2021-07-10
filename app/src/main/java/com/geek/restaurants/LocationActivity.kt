package com.geek.restaurants

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.mongodb.User
import kotlinx.android.synthetic.main.activity_location.*

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

    }

    override fun onStart() {
        super.onStart()

        user = restaurantApp.currentUser()
        if(user == null){
            // if no user is currently logged in, start the login activity so the user can authenticate
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


}