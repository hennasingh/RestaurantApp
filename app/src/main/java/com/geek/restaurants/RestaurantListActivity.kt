package com.geek.restaurants

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.mongodb.User

class RestaurantListActivity : AppCompatActivity() {

    private var user: User? = null
    private var locRealm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)
    }

    override fun onStart() {
        super.onStart()

        user = restaurantApp.currentUser()
        if(user == null){
            // if no user is currently logged in, start the login activity so the user can authenticate
            startActivity(Intent(this, LoginActivity::class.java))
        } else {

        }
    }
}