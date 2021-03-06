package com.geek.restaurants.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geek.restaurants.R
import com.geek.restaurants.model.Restaurant
import com.geek.restaurants.restaurantApp
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_restaurant_list.*
import timber.log.Timber
import java.io.File

class RestaurantListActivity : AppCompatActivity() {

    private var user: User? = null
    private lateinit var adapter: RestaurantAdapter
   private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        checkUser()

        val extras = intent.getStringExtra("EXTRA_PARTITION")

        title = extras!!
        Timber.d("partition is $extras")
        recyclerView = rv_list
       recyclerView.layoutManager = LinearLayoutManager(this)
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

                    val restaurantList: RealmResults<Restaurant> = realm.where<Restaurant>().findAll()
                    updateUI(restaurantList)
                }
            })
    }

    private fun updateUI(restaurantList: RealmResults<Restaurant>) {

        Timber.d("Synced restaurants count ${restaurantList.size}")
        progress.visibility = View.GONE

        //Timber.d(restaurantList.asJSON())

         adapter = RestaurantAdapter(restaurantList)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }
}