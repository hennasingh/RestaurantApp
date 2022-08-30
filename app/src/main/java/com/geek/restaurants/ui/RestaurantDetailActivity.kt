package com.geek.restaurants.ui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geek.restaurants.R
import com.geek.restaurants.model.Restaurant
import com.geek.restaurants.model.Reviews
import com.geek.restaurants.restaurantApp
import io.realm.OrderedCollectionChangeSet
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_restaurant_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class RestaurantDetailActivity : AppCompatActivity() {

    private var restID: String? = null

    private lateinit var config: SyncConfiguration
    private lateinit var realm: Realm
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReviewsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        restID = intent.getStringExtra("RESTID")
        Timber.d("RestID $restID")

        Timber.d("Setting Realm instance again")
        restaurantApp.currentUser()?.apply {
            config = SyncConfiguration.Builder(this).build()
        }
        recyclerView = rvReviews
        addReviewBtn.setOnClickListener { 
            val input = EditText(this)
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Add a review for this restaurant:")
                .setCancelable(true)
                .setPositiveButton("Add"){dialog, _->
                    val review = input.text.toString()
                    dialog.dismiss()
                    addReviewToDatabase(review)
                }
                .setNegativeButton("Cancel"){dialog, _ ->
                    dialog.cancel()
                }
            val dialog = dialogBuilder.create()
            dialog.setView(input)
            dialog.setTitle("Add Restaurant Review")
            dialog.show()
        }

    }

    private fun addReviewToDatabase(review: String) {

        val userReview = Reviews(review, restaurantApp.currentUser()?.id, restaurantApp.currentUser()?.profile?.name)
        CoroutineScope(Dispatchers.IO).launch {
            realm.copyToRealmOrUpdate(userReview)
        }
    }

    override fun onStart() {
        super.onStart()
        getRestDetail(config)

    }

    private fun getRestDetail(config: SyncConfiguration) {

       Realm.getInstanceAsync(config, object: Realm.Callback() {
           override fun onSuccess(realm: Realm) {
               val restaurant = realm.where<Restaurant>().equalTo("restaurant_id", restID).findFirst()
               Timber.d("%s", restaurant)

               restaurant?.apply {
                   title = this.name
                   tv_name.text = this.name
                   """${address?.building} ${address?.street} ${address?.zipcode}""".also {
                       tvAddress.text = it
                   }
                   if(reviews.size == 0) {
                       tvNoReview.visibility = View.VISIBLE
                       progress.visibility = View.GONE
                   } else {
                       displayReviewAdapter(reviews)
                   }
               }
               callChangeListener(restaurant)

           }

       })

    }

    private fun callChangeListener(restaurant: Restaurant?) {

        val listener = OrderedRealmCollectionChangeListener{ _: RealmList<Reviews>?, changeSet: OrderedCollectionChangeSet? ->
            for(range in changeSet!!.insertionRanges){
                if(restaurant?.reviews?.size==0){
                    tvNoReview.visibility = View.VISIBLE
                    progress.visibility = View.GONE
                }else {
                    displayReviewAdapter(restaurant!!.reviews)
                }
            }
        }
        restaurant!!.reviews.addChangeListener(listener)
    }

    private fun displayReviewAdapter(reviews: RealmList<Reviews>) {
        adapter = ReviewsAdapter(reviews)
        tvNoReview.visibility = View.GONE
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
      progress.visibility = View.GONE

        recyclerView.layoutManager = LinearLayoutManager(this@RestaurantDetailActivity)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(this@RestaurantDetailActivity, DividerItemDecoration.VERTICAL))
    }
}