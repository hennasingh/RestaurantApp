package com.geek.restaurants.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.geek.restaurants.R
import com.geek.restaurants.model.Reviews
import io.realm.RealmList
import io.realm.RealmRecyclerViewAdapter

class ReviewsAdapter(dataSet: RealmList<Reviews>): RealmRecyclerViewAdapter<Reviews, ReviewHolder?>(dataSet, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.review_view, parent, false)
        return ReviewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
        holder.bindValues(getItem(position)!!)
    }
}