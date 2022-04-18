package com.geek.restaurants

import android.app.Application
import io.realm.Realm
import io.realm.log.LogLevel
import io.realm.log.RealmLog
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import timber.log.Timber

const val appId = "restaurantfinder-hjoqd"
lateinit var restaurantApp: App

class RestaurantApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        //RealmLog.setLevel(LogLevel.ALL)

        Timber.plant(Timber.DebugTree())


        restaurantApp = App(AppConfiguration.Builder(appId).build())

    }
}