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
import io.realm.ObjectChangeSet
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.where
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.android.synthetic.main.activity_restaurant_detail.*
import timber.log.Timber

class RestaurantDetailActivity : AppCompatActivity() {

    private var restID: String? = null

    private lateinit var config: SyncConfiguration
    private lateinit var realm: Realm
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReviewsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        // UI THREAD
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        restID = intent.getStringExtra("RESTID")
        Timber.d("RestID $restID")

        recyclerView = rvReviews

        realm = Realm.getDefaultInstance()

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

    override fun onDestroy() {
        super.onDestroy()

        realm.close()
    }

    private fun addReviewToDatabase(review: String) {
        realm.executeTransactionAsync { transactionRealm: Realm ->
            // NOT UI THREAD - BG THREAD
            val restaurant = transactionRealm.where<Restaurant>()
                .equalTo("restaurant_id", restID)
                .findFirst()

            val userReview = Reviews(review, restaurantApp.currentUser()?.id, restaurantApp.currentUser()?.profile?.name)
            restaurant!!.reviews.add(userReview)
        }
    }

    override fun onStart() {
        super.onStart()
        getRestDetail()
    }

    private fun getRestDetail() {
        val restaurant = realm.where<Restaurant>()
            .equalTo("restaurant_id", restID)
            .findFirstAsync()

        restaurant.addChangeListener { restaurant: Restaurant, changeSet: ObjectChangeSet? ->
            // UI Thread - Looper
            if (restaurant.isValid) {

                title = restaurant.name
                tv_name.text = restaurant.name
                """${restaurant.address?.building} ${restaurant.address?.street} ${restaurant.address?.zipcode}""".also {
                    tvAddress.text = it
                }

                if (restaurant.reviews.size == 0) {
                    tvNoReview.visibility = View.VISIBLE
                    progress.visibility = View.GONE
                } else {
                    displayReviewAdapter(restaurant.reviews)
                }
            }
        }

//            callChangeListener(restaurant)

        }


//    private fun callChangeListener(restaurant: Restaurant?) {
//
//        val listener = OrderedRealmCollectionChangeListener{ _: RealmList<Reviews>?, changeSet: OrderedCollectionChangeSet? ->
//            for(range in changeSet!!.insertionRanges){
//                if(restaurant?.reviews?.size==0){
//                    tvNoReview.visibility = View.VISIBLE
//                    progress.visibility = View.GONE
//                }else {
//                    displayReviewAdapter(restaurant!!.reviews)
//                }
//            }
//        }
//        restaurant!!.reviews.addChangeListener(listener)
//    }

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