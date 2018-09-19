package com.example.delivery

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration


/**
 * Created by Ganesh Tikone on 17/9/18.
 * Class: DeliveryApplication
 */
class DeliveryApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initialiseRealmDatabase()
    }

    /**
     * Initialise RealmDatabase with Configuration
     */
    private fun initialiseRealmDatabase() {
        Realm.init(this)

        val config = RealmConfiguration.Builder()
                .name(getString(R.string.app_name))
                .schemaVersion(0)
                .build()

        // Use the config
        Realm.getInstance(config)
    }
}