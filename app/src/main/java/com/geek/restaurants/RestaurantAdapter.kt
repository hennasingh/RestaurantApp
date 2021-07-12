package com.geek.restaurants

import android.view.LayoutInflater
import android.view.ViewGroup
import com.geek.restaurants.model.Restaurant
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class RestaurantAdapter(data: OrderedRealmCollection<Restaurant>):RealmRecyclerViewAdapter<Restaurant, RestaurantHolder>(data, true) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.location_item, parent, false)
        return RestaurantHolder(view)

    }


    override fun onBindViewHolder(holder: RestaurantHolder, position: Int) {
        val restaurant = getItem(position)
        holder.bindValues(restaurant!!)
    }
}