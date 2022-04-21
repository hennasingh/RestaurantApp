package com.geek.restaurants

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.geek.restaurants.model.Borough
import com.geek.restaurants.model.Restaurant
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_location.*
import org.bson.Document
import timber.log.Timber
import android.widget.LinearLayout
import androidx.core.view.marginStart


class LocationActivity : AppCompatActivity() {

    private var user: User? = null
    private lateinit var config : SyncConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        user = restaurantApp.currentUser()
        Timber.d("User is $user")
        if(user == null){
            // if no user is currently logged in, start the login activity so the user can authenticate
            startActivity(Intent(this, LoginActivity::class.java))
        }else {

            config = SyncConfiguration.Builder(user, "borough")
                .waitForInitialRemoteData()
                .build()
        }

        Timber.d("Opening Realm instance Asynchronously")

        Realm.getInstanceAsync(config, object: Realm.Callback() {
            override fun onSuccess(realm: Realm) {

                val locationList: RealmResults<Borough> = realm.where<Borough>().findAll()
                locationList.forEach{borough ->
                    setUpUI(borough.location)
                }
            }
        })
    }

    private fun setUpUI(location: String) {

        // create the layout params that will be used to define how your
        // button will be displayed
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.marginStart = 30
        params.marginEnd = 30
        val button = Button(this)
        button.text = location
        button.layoutParams = params

        button.setOnClickListener{
            openPartition(location)
        }
        linearLayout.addView(button)

    }

    private fun openPartition(partition: String) {

        val intent = Intent(this, RestaurantListActivity::class.java)
        intent.putExtra("EXTRA_PARTITION",partition!!)
        startActivity(intent)
    }


}