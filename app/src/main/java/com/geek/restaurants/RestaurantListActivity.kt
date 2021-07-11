package com.geek.restaurants

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geek.restaurants.model.Restaurant
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_restaurant_list.*
import timber.log.Timber

class RestaurantListActivity : AppCompatActivity() {

    private var user: User? = null
    private var borough: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        checkUser()

        val extras = intent.getStringExtra("EXTRA_PARTITION")

        title = extras
        Timber.d("partition is $extras")
        displayRestaurantsAt(extras)
    }

    private fun checkUser() {
        user = restaurantApp.currentUser()
        Timber.d("User is $user")
        if(user == null){
            // if no user is currently logged in, start the login activity so the user can authenticate
            Timber.d("User is null")
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun displayRestaurantsAt(partition: String) {

        Timber.d("$user")
        Timber.d("Setting Realm Configurtion with partition $partition")
        val config = SyncConfiguration.Builder(user, partition)
            .waitForInitialRemoteData()
            .build()

        Timber.d("Opening Realm instance Asynchronously")
        Realm.getInstanceAsync(config, object:Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val restaurantList: RealmResults<Restaurant> = realm.where<Restaurant>().findAll()
                Timber.d("Synced restaurants count ${restaurantList.size}")
                Timber.d(restaurantList.asJSON())
                updateUI(restaurantList)
            }
        })
    }

    private fun updateUI(restaurantList: RealmResults<Restaurant>) {

        tv_name.text = restaurantList[0]?.name
        tv_cuisine.text = restaurantList[0]?.cuisine
    }
}