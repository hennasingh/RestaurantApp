package com.geek.restaurants

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.geek.restaurants.model.Restaurant
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import timber.log.Timber

class RestaurantListActivity : AppCompatActivity() {

    private var user: User? = null
    private var borough: Realm? = null
    //private lateinit var adapter: RestaurantAdapter
   // private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        checkUser()

        val extras = intent.getStringExtra("EXTRA_PARTITION")

        title = extras
        Timber.d("partition is $extras")
       // recyclerView.layoutManager = LinearLayoutManager(this)
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
        Timber.d("Setting Realm Configuration with partition $partition")
        val config = SyncConfiguration.Builder(user, partition)
            .waitForInitialRemoteData()
            .build()

        Timber.d("Opening Realm instance Asynchronously")
        val handler = Handler()
        handler.postDelayed(Runnable {
            Realm.getInstanceAsync(config, object:Realm.Callback() {
                override fun onSuccess(realm: Realm) {
                    borough = realm
                   // updateUI(borough!!)
                    val restaurantList: RealmResults<Restaurant> = realm.where<Restaurant>().findAll()
                    updateUI(restaurantList)
                }
            })
        }, 60000)
    }

    private fun updateUI(restaurantList: RealmResults<Restaurant>) {

        Timber.d("Synced restaurants count ${restaurantList.size}")
        Timber.d(restaurantList.asJSON())

       // adapter = RestaurantAdapter(restaurantList)
      //  recyclerView.adapter = adapter


    }

    override fun onDestroy() {
        super.onDestroy()
        borough!!.close()
    }
}