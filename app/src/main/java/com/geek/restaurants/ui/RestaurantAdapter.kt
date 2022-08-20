package com.geek.restaurants.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import com.geek.restaurants.R
import com.geek.restaurants.model.Restaurant
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import timber.log.Timber

class RestaurantAdapter(data: OrderedRealmCollection<Restaurant>):RealmRecyclerViewAdapter<Restaurant, RestaurantHolder>(data, true) {

    lateinit var parent: ViewGroup

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantHolder {

        this.parent = parent
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false)
        return RestaurantHolder(view)

    }

    override fun onBindViewHolder(holder: RestaurantHolder, position: Int) {
        val restaurant = getItem(position)
        holder.bindValues(restaurant!!)
    }
}