package com.geek.restaurants.ui


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.realm.mongodb.User
import kotlinx.android.synthetic.main.activity_location.*
import android.widget.Toast
import com.geek.restaurants.R
import com.geek.restaurants.model.Restaurant
import com.geek.restaurants.restaurantApp


class LocationActivity : AppCompatActivity() {

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        user = restaurantApp.currentUser()

        btnSearch.setOnClickListener {
            validateAndSearch()
        }
    }

    private fun validateAndSearch() {
        if(!credentialsValid()){
            fieldsEmptyError("Both fields cannot be empty")
            return
        }
        val location = etlocation.text.toString()
        val food = etFood.text.toString()

        //once validated, pass the values to List Activity to display

        val intent = Intent(applicationContext, RestaurantListActivity::class.java)
        intent.putExtra("LOCATION", location)
        intent.putExtra("FOOD", food)
        startActivity(intent)

    }
    private fun fieldsEmptyError(errorMsg: String) {

        Toast.makeText(baseContext, errorMsg, Toast.LENGTH_LONG).show()
    }

    private fun credentialsValid(): Boolean = when {
        etFood.text.toString().isEmpty() && etlocation.text.isEmpty() -> false
        else-> true
    }

}