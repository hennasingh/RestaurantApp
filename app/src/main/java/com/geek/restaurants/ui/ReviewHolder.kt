package com.geek.restaurants.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.geek.restaurants.model.Reviews
import kotlinx.android.synthetic.main.review_view.view.*

class ReviewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindValues(review: Reviews){

        itemView.tvReview.text = review.review
        itemView.tvUsername.text = review.username
    }
}
