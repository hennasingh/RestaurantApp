package com.geek.restaurants

import android.app.Application
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.sync.ClientResetRequiredError
import io.realm.mongodb.sync.DiscardUnsyncedChangesStrategy
import io.realm.mongodb.sync.SyncSession
import timber.log.Timber

const val appId = "restra-flexi-shytm"
lateinit var restaurantApp: App

class RestaurantApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        //RealmLog.setLevel(LogLevel.ALL)

        Timber.plant(Timber.DebugTree())


        restaurantApp = App(AppConfiguration.Builder(appId)
            .defaultSyncClientResetStrategy(object: DiscardUnsyncedChangesStrategy {
                override fun onBeforeReset(realm: Realm) {
                    Timber.d("Beginning Client reset for %s", realm.path)
                }

                override fun onAfterReset(before: Realm, after: Realm) {
                    Timber.d("Completed Client reset for %s", after.path)
                }

                override fun onError(session: SyncSession, error: ClientResetRequiredError) {
                    Timber.e("Client reset not handled automatically, falling back to manual %s", error.errorMessage)

                    try {
                        // execute the client reset, moving the current realm to a backup file
                        error.executeClientReset()

                    } catch (exception: IllegalStateException) {
                        Timber.e("Failed to execute the client-reset: %s ", exception.message)
                    }
                }

            })
        .build()
        )

    }
}