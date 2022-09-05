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
import io.realm.OrderedCollectionChangeSet
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.syncSession
import io.realm.mongodb.User
import io.realm.mongodb.sync.Subscription
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_restaurant_list.*
import timber.log.Timber

class RestaurantListActivity : AppCompatActivity() {

    private lateinit var restaurantList: RealmResults<Restaurant>
    private var food: String? = null
    private var location: String? = null
    private var user: User? = null
    private lateinit var adapter: RestaurantAdapter
   private lateinit var recyclerView: RecyclerView
   private lateinit var config: SyncConfiguration
   private lateinit var realm : Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        location = intent.getStringExtra("LOCATION")
        food = intent.getStringExtra("FOOD")

        title = location
        recyclerView = rv_list
       recyclerView.layoutManager = LinearLayoutManager(this)

        checkUser()
    }

    private fun downloadChangesInBackground() {
        Thread {
            Realm.getInstance(config).use{ realm ->
                realm.syncSession.downloadAllServerChanges()
            }
        }.start()
    }

    private fun checkUser() {
        user = restaurantApp.currentUser()
        Timber.d("User is $user")
        if(user == null){
            // if no user is currently logged in, start the login activity so the user can authenticate
            Timber.d("User is null")
            startActivity(Intent(this, LoginActivity::class.java))
        }
        else {
            initializeRealm()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun initializeRealm() {
        config = SyncConfiguration.Builder(user!!)
            .initialSubscriptions {realm, subscriptions ->
                    subscriptions.addOrUpdate(
                        Subscription.create(
                            "restaurantSubscription",
                            realm.where(Restaurant::class.java)
                        )
                    )
            }
            .build()
        Realm.setDefaultConfiguration(config)

        //downloadChangesInBackground() //this is not actually needed

        //Instantiate a realm instance with the flexible sync configuration
        Realm.getInstanceAsync(config, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                this@RestaurantListActivity.realm = realm
                onAfterRealmInit()
            }
        })
    }

    private fun onAfterRealmInit() {


//        val restaurantList = realm.where(Restaurant::class.java)
//            .equalTo("borough", location)
//            .and()
//            .equalTo("cuisine", food)
//            .findAll()

        restaurantList = realm.where(Restaurant::class.java)
            .equalTo("borough", location)
            .and()
            .equalTo("cuisine", food)
            .findAllAsync()

        restaurantList.addChangeListener { t: RealmResults<Restaurant>, _: OrderedCollectionChangeSet ->
            updateUI(t)
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