package com.cafstone.application.di

import android.content.Context
import com.cafstone.application.data.UserRepository
import com.cafstone.application.data.pref.UserPreference
import com.cafstone.application.data.pref.dataStore
import com.cafstone.application.data.retrofit.ApiConfig
import com.cafstone.application.data.retrofit.ApiConfig2

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        // Pastikan Anda memiliki definisi com.cafstone.application.api.ApiConfig yang sesuai di sini
        val apiService = ApiConfig.getApiService()
        val apiService2 = ApiConfig2.getApiService()
        return UserRepository.getInstance(pref, apiService,apiService2)
    }
}