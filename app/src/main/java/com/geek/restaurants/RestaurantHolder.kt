package com.geek.restaurants

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.geek.restaurants.model.Restaurant
import kotlinx.android.synthetic.main.location_item.view.*

class RestaurantHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val resName = itemView.tv_name
    private val resCuisine = itemView.tv_cuisine

    fun bindValues(Restaurant: Restaurant){
        resName.text = Restaurant.name
        resCuisine.text = Restaurant.cuisine
    }
}