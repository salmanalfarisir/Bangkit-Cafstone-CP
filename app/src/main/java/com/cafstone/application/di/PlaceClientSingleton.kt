package com.cafstone.application.di

import android.content.Context
import com.cafstone.application.BuildConfig
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

object PlacesClientSingleton {
    private var placesClient: PlacesClient? = null

    fun getInstance(context: Context): PlacesClient {
        if (placesClient == null) {
            synchronized(this) {
                if (placesClient == null) {
                    if (!Places.isInitialized()) {
                        Places.initializeWithNewPlacesApiEnabled(
                            context.applicationContext,
                            BuildConfig.MAPS_API_KEY
                        )
                    }
                    placesClient = Places.createClient(context.applicationContext)
                }
            }
        }
        return placesClient!!
    }

}