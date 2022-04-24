package com.geek.restaurants.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.geek.restaurants.R
import com.geek.restaurants.restaurantApp
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        user = restaurantApp.currentUser()

        if(user != null){
            startActivity(Intent(this, LocationActivity::class.java))
        }

        button_login.setOnClickListener{
            login(false)
        }
        button_create.setOnClickListener{
            login(true)
        }
    }

    private fun login(createUser: Boolean) {

        if(!validateCredentials()){
            onLoginFailed("Fields cannot be empty")
            return
        }

        // while this operation completes, disable the buttons to login or create a new account
        button_login.isEnabled = false
        button_create.isEnabled = false

        val email = username.text.toString()
        val password = password.toString()


        if(createUser){
            //register  a user
            restaurantApp.emailPassword.registerUserAsync(email, password){
                if(it.isSuccess){
                    Timber.d("User successfully registered")
                    // when the account has been created successfully, log in to the account
                    login(false)
                }else {
                    onLoginFailed(it.error.errorMessage ?: "An error occurred on registering")
                    enableButtons()
                }
            }

        } else {
            val credentials = Credentials.emailPassword(email, password)
            restaurantApp.loginAsync(credentials){
                if(!it.isSuccess){
                    enableButtons()
                    onLoginFailed(it.error.errorMessage ?: "An error occurred")
                } else {
                    startActivity(Intent(this, LocationActivity::class.java))
                }
            }
        }
    }

    private fun enableButtons() {
        button_create.isEnabled = true
        button_login.isEnabled = true
    }

    private fun validateCredentials(): Boolean = when {
        // zero-length usernames and passwords are not valid (or secure), so prevent users from creating accounts with those client-side.
        username.text.toString().isEmpty() -> false
        password.text.toString().isEmpty() -> false
        else -> true
    }


    private fun onLoginFailed(errorMsg: String) {
        Timber.e(errorMsg)
        Toast.makeText(baseContext, errorMsg, Toast.LENGTH_LONG).show()
    }
}