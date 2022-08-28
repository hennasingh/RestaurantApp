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
import io.realm.mongodb.User
import io.realm.mongodb.sync.Subscription
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_restaurant_list.*
import timber.log.Timber

class RestaurantListActivity : AppCompatActivity() {

    private var user: User? = null
    private lateinit var adapter: RestaurantAdapter
   private lateinit var recyclerView: RecyclerView
   private lateinit var config: SyncConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        checkUser()

        val location = intent.getStringExtra("LOCATION")
        val food = intent.getStringExtra("FOOD")

        title = location
        recyclerView = rv_list
       recyclerView.layoutManager = LinearLayoutManager(this)
        displayRestaurants(food!!, location!!)
        
    }

    private fun displayRestaurants(food: String, location: String) {

        //Instantiate a realm instance with the flexible sync configuration

        Realm.getInstanceAsync(config, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val restaurantList = realm.where(Restaurant::class.java)
                    .equalTo("borough", location)
                    .and()
                    .equalTo("cuisine", food)
                    .findAll()
                updateUI(restaurantList)
            }

        })
    }

    private fun checkUser() {
        user = restaurantApp.currentUser()
        Timber.d("User is $user")
        if(user == null){
            // if no user is currently logged in, start the login activity so the user can authenticate
            Timber.d("User is null")
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
             config = SyncConfiguration.Builder(user!!)
                .initialSubscriptions {realm, subscriptions ->
                    if (subscriptions.find("locationSubscription")==null)
                        subscriptions.add(
                            Subscription.create(
                                "locationSubscription",
                                realm.where(Restaurant::class.java)
                            )
                        )
                }
                .build()
        }
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