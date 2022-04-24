package com.geek.restaurants.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.geek.restaurants.model.Borough
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_location.*
import timber.log.Timber
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.geek.restaurants.R
import com.geek.restaurants.model.Restaurant
import com.geek.restaurants.restaurantApp
import kotlinx.android.synthetic.main.activity_location.progress
import kotlinx.android.synthetic.main.activity_restaurant_list.*
import java.io.File


class LocationActivity : AppCompatActivity() {

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        user = restaurantApp.currentUser()
        Timber.d("User is $user")
        if (user == null) {
            // if no user is currently logged in, start the login activity so the user can authenticate
            Timber.d("User is  $user, calling Login")
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun displayLocations(partition: String) {

        Timber.d("Setting Realm Configuration with partition $partition")
        val config = SyncConfiguration.Builder(user, partition)
            .waitForInitialRemoteData()
            .build()

        Timber.d("Opening Realm instance Asynchronously")
        /**
         * Should print false if its opening for the first time
         */
        Timber.d("${File(config.path).exists()}")

        Realm.getInstanceAsync(config, object:Realm.Callback() {
            override fun onSuccess(realm: Realm) {

                val locationList: RealmResults<Borough> = realm.where<Borough>().findAll()
                   checkLocationList(locationList)
            }
        })
    }

    private fun checkLocationList(locationList: RealmResults<Borough>) {

        Timber.d("Synced restaurants count ${locationList.size}")
        progress.visibility = View.GONE

        locationList.forEach { borough ->
            setUpUI(borough.location)

        }
    }

    private fun setUpUI(location: String) {

        // create the layout params that will be used to define how your
        // button will be displayed
        val params: ViewGroup.MarginLayoutParams= ViewGroup.MarginLayoutParams(
            ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT
        )

        params.setMargins(30,15,30,15)

        val button = Button(this)
        button.text = location
        button.layoutParams = params
        button.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent))

        linearLayout.addView(button)
        button.setOnClickListener{
            openPartition(location)
        }

    }

    private fun openPartition(partition: String) {

        val intent = Intent(this, RestaurantListActivity::class.java)
        intent.putExtra("EXTRA_PARTITION",partition!!)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        displayLocations("borough")
    }


}