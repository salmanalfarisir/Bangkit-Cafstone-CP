package com.cafstone.application.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[servesBeer_key] = user.servesBeer
            preferences[servesWine_key] = user.servesWine
            preferences[servesCocktails_key] = user.servesCocktails
            preferences[goodForChildren_key] = user.goodForChildren
            preferences[goodForGroups_key] = user.goodForGroups
            preferences[reservable_key] = user.reservable
            preferences[outdoorSeating_key] = user.outdoorSeating
            preferences[liveMusic_key] = user.liveMusic
            preferences[servesDessert_key] = user.servesDessert
            preferences[priceLevel_key] = user.priceLevel
            preferences[acceptsCreditCards_key] = user.acceptsCreditCards
            preferences[acceptsDebitCards_key] = user.acceptsDebitCards
            preferences[acceptsCashOnly_key] = user.acceptsCashOnly
            preferences[acceptsNfc_key] = user.acceptsNfc
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[NAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[servesBeer_key] ?: false,
                preferences[servesWine_key] ?: false,
                preferences[servesCocktails_key] ?: false,
                preferences[goodForChildren_key] ?: false,
                preferences[goodForGroups_key] ?: false,
                preferences[reservable_key] ?: false,
                preferences[outdoorSeating_key] ?: false,
                preferences[liveMusic_key] ?: false,
                preferences[servesDessert_key] ?: false,
                preferences[priceLevel_key] ?: 0,
                preferences[acceptsCreditCards_key] ?: false,
                preferences[acceptsDebitCards_key] ?: false,
                preferences[acceptsCashOnly_key] ?: false,
                preferences[acceptsNfc_key] ?: false,
                preferences[welcome_key] ?:"Welcome",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun getData(): UserModel {
        val preferences = dataStore.data.first()
        return UserModel(
            preferences[NAME_KEY] ?: "",
            preferences[EMAIL_KEY] ?: "",
            preferences[servesBeer_key] ?: false,
            preferences[servesWine_key] ?: false,
            preferences[servesCocktails_key] ?: false,
            preferences[goodForChildren_key] ?: false,
            preferences[goodForGroups_key] ?: false,
            preferences[reservable_key] ?: false,
            preferences[outdoorSeating_key] ?: false,
            preferences[liveMusic_key] ?: false,
            preferences[servesDessert_key] ?: false,
            preferences[priceLevel_key] ?: 0,
            preferences[acceptsCreditCards_key] ?: false,
            preferences[acceptsDebitCards_key] ?: false,
            preferences[acceptsCashOnly_key] ?: false,
            preferences[acceptsNfc_key] ?: false,
            preferences[welcome_key] ?:"Welcome",
            preferences[IS_LOGIN_KEY] ?: false
        )
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val servesBeer_key = booleanPreferencesKey("serverBeer")
        private val servesWine_key = booleanPreferencesKey("servesWine")
        private val servesCocktails_key = booleanPreferencesKey("servesCocktails")
        private val goodForChildren_key = booleanPreferencesKey("goodForChildren")
        private val goodForGroups_key = booleanPreferencesKey("goodForGroups")
        private val reservable_key = booleanPreferencesKey("reservable")
        private val outdoorSeating_key = booleanPreferencesKey("outdoorSeating")
        private val liveMusic_key = booleanPreferencesKey("liveMusic")
        private val servesDessert_key = booleanPreferencesKey("servesDessert")
        private val priceLevel_key = intPreferencesKey("price_level")
        private val acceptsCreditCards_key = booleanPreferencesKey("acceptsCreditCards")
        private val acceptsDebitCards_key = booleanPreferencesKey("acceptsDebitCards")
        private val acceptsCashOnly_key = booleanPreferencesKey("acceptsCashOnly")
        private val acceptsNfc_key = booleanPreferencesKey("acceptsNfc")
        private val welcome_key = stringPreferencesKey("welcome")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}