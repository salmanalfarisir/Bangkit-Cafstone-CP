package com.cafstone.application.data

import android.content.ContentValues.TAG
import android.util.Log
import com.cafstone.application.data.pref.UserModel
import com.cafstone.application.data.pref.UserPreference
import com.cafstone.application.data.response.LoginResponse
import com.cafstone.application.data.response.RecomendationResponseItem
import com.cafstone.application.data.response.RegisterResponse
import com.cafstone.application.data.retrofit.ApiService
import com.cafstone.application.data.retrofit.ApiService2
import com.cafstone.application.view.main.RecomendationModel
import com.cafstone.application.view.signup.UserRegisterModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val apiService2: ApiService2
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun getdata(): UserModel {
        val data = runBlocking { userPreference.getdata() }
        return data
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun recomendation (list : RecomendationModel) : List<RecomendationResponseItem>?{
        return try {
            val response = apiService2.getrecomendation(list)
            Log.d(TAG,"Berhasil BANG ANAJAYYY")
            response
        }catch (e: Exception){
            Log.d(TAG,"${e.message}")
            null
        }
    }
    suspend fun login(email: String, password: String): LoginResponse {
        val response = apiService.login(email, password)
        if (response.success) {
            // Jika login berhasil dan token diterima, simpan token ke dalam DataStore
            val user = UserModel(
                response.data.name,
                email,
                response.data.preferences.servesBeer,
                response.data.preferences.servesWine,
                response.data.preferences.servesCocktails,
                response.data.preferences.goodForChildren,
                response.data.preferences.goodForGroups,
                response.data.preferences.reservable,
                response.data.preferences.outdoorSeating,
                response.data.preferences.liveMusic,
                response.data.preferences.servesDessert,
                response.data.preferences.priceLevel,
                response.data.preferences.acceptsCreditCards,
                response.data.preferences.acceptsDebitCards,
                response.data.preferences.acceptsCashOnly,
                response.data.preferences.acceptsNfc,
                true
            )

            Log.d(TAG, user.toString())
            saveSession(user)
        }
        return response
    }

    suspend fun register(user: UserRegisterModel): RegisterResponse {
        return apiService.register(user)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
            apiService2: ApiService2
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService,apiService2)
            }.also { instance = it }
    }
}