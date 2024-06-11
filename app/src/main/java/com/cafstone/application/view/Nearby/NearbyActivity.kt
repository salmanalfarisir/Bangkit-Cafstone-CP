package com.cafstone.application.view.Nearby

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafstone.application.data.adapter.AdapterModel
import com.cafstone.application.data.adapter.PlacesAdapter
import com.cafstone.application.databinding.ActivityNearbyBinding
import com.cafstone.application.di.PlacesClientSingleton
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest

class NearbyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNearbyBinding
    private val placesList = mutableListOf<AdapterModel>()
    private lateinit var adapter: PlacesAdapter
    private lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearbyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = PlacesAdapter(placesList)
        val lat = intent.getDoubleExtra(LATITUDE,0.0)
        val long = intent.getDoubleExtra(LONGTITUDE,0.0)
        binding.rvReview.layoutManager = LinearLayoutManager(this)
        binding.rvReview.adapter = adapter

        if (lat!=0.0 && long!=0.0) {
            val data = intent.getStringExtra(EXTRA_DETAIL)
            nearby(data,lat,long)
        } else {
            finish()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun nearby(text: String?, lat:Double, long:Double) {

        placesClient = PlacesClientSingleton.getInstance(this)

        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.TYPES,
            Place.Field.PHOTO_METADATAS,
            Place.Field.RATING
        )
        val searchCenter = LatLng(lat, long)
        val circle = CircularBounds.newInstance(searchCenter, 50000.0)
        val includedTypes = listOf(
            "restaurant", "american_restaurant", "bar", "sandwich_shop", "coffee_shop",
            "fast_food_restaurant", "seafood_restaurant", "steak_house", "sushi_restaurant",
            "vegetarian_restaurant", "ice_cream_shop", "japanese_restaurant", "korean_restaurant",
            "brazilian_restaurant", "mexican_restaurant", "breakfast_restaurant",
            "middle_eastern_restaurant", "brunch_restaurant", "pizza_restaurant", "cafe",
            "ramen_restaurant", "chinese_restaurant", "mediterranean_restaurant", "meal_delivery",
            "meal_takeaway", "barbecue_restaurant", "spanish_restaurant", "greek_restaurant",
            "hamburger_restaurant", "thai_restaurant", "indian_restaurant", "turkish_restaurant",
            "indonesian_restaurant", "vegan_restaurant", "italian_restaurant"
        )

        val searchNearbyRequest = searchnearby(circle, placeFields, includedTypes, text)

        placesClient.searchNearby(searchNearbyRequest)
            .addOnSuccessListener { response ->
                val places = response.places
                Log.d(TAG, "Number of places found: ${places.size}")
                for (place in places) {
                    if (place.placeTypes != null) {
                        var photoUrl : PhotoMetadata? = null
                        if (!place.photoMetadatas.isNullOrEmpty())
                        {
                            photoUrl = place.photoMetadatas?.get(0)
                        }

                        Log.d(TAG, "Adding place: ${place.name}")
                        placesList.add(
                            AdapterModel(
                                place.id!!,
                                place.name!!,
                                place.address!!,
                                photoUrl,
                                place.rating
                            )
                        )
                    }
                }
                adapter.notifyDataSetChanged()
                Log.d(TAG,"Adapter updated")
            }
            .addOnFailureListener { exception: Exception ->
                Log.e(TAG, "Place not found: ${exception.message}")
            }
    }

    @SuppressLint("SetTextI18n")
    fun searchnearby(
        circle: CircularBounds,
        placeFields: List<Place.Field>,
        includedTypes: List<String>,
        kriteria: String?
    ): SearchNearbyRequest {
        var searchNearbyRequest: SearchNearbyRequest =
            SearchNearbyRequest.builder(circle, placeFields)
                .setIncludedTypes(includedTypes)
                .setMaxResultCount(20)
                .build()
        if (kriteria.equals("terdekat")) {
            binding.tvItemName.text = "Terdekat"
            binding.textView.text = "Butuh Cafe Terdekat? Ini Rekomendasinya"
            searchNearbyRequest =
                SearchNearbyRequest.builder(circle, placeFields)
                    .setIncludedTypes(includedTypes)
                    .setMaxResultCount(20)
                    .setRankPreference(SearchNearbyRequest.RankPreference.DISTANCE)
                    .build()
        }
        return searchNearbyRequest
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        const val LATITUDE = "lat"
        const val LONGTITUDE = "LONG"
    }
}
