package com.geek.restaurants.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.geek.restaurants.model.Restaurant
import kotlinx.android.synthetic.main.location_item.view.*

class RestaurantHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val resName = itemView.tv_name
    private val resCuisine = itemView.tv_cuisine
    private val resStreet = itemView.tv_street

    fun bindValues(restaurant: Restaurant){
        resName.text = restaurant.name
        resCuisine.text = restaurant.cuisine
        val street = "${restaurant.borough}  ${restaurant.address?.street}"
        resStreet.text = street
    }
}