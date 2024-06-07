package com.cafstone.application.di

import android.content.Context
import android.location.Location
import com.google.gson.Gson

object LocationSharedPreferences {

    private const val SHARED_PREFS_NAME = "LocationPrefs"
    private const val LOCATION_KEY = "location"

    fun saveOrUpdateLocation(context: Context, latitude: Double, longitude: Double) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Buat objek lokasi dari koordinat
        val location = Location("")
        location.latitude = latitude
        location.longitude = longitude

        // Konversi lokasi menjadi JSON string
        val gson = Gson()
        val locationString = gson.toJson(location)

        // Simpan atau perbarui lokasi di SharedPreferences
        editor.putString(LOCATION_KEY, locationString)
        editor.apply()
    }

    fun getLocation(context: Context): Location? {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val locationString = sharedPreferences.getString(LOCATION_KEY, null) ?: return null

        // Jika tidak ada lokasi yang disimpan, kembalikan null

        // Jika ada lokasi yang disimpan, kembalikan objek lokasi dari JSON string
        return gson.fromJson(locationString, Location::class.java)
    }

    fun clearLocation(context: Context) {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Hapus lokasi dari SharedPreferences
        editor.remove(LOCATION_KEY)
        editor.apply()
    }
}