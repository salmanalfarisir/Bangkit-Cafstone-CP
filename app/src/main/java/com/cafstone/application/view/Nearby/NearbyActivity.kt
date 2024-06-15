package com.cafstone.application.view.Nearby

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Build
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
import com.google.android.libraries.places.api.net.SearchByTextRequest
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
            val att = if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra(EXTRA_ATT, NearbyModel::class.java)
            } else {
                @Suppress("DEPRECATION")
                intent.getParcelableExtra(EXTRA_ATT)
            }
            nearby(data,att,lat,long)
        } else {
            finish()
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun nearby(text: String?,att:NearbyModel?, lat:Double, long:Double) {

        placesClient = PlacesClientSingleton.getInstance(this)

        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.TYPES,
            Place.Field.PHOTO_METADATAS,
            Place.Field.RATING,
            Place.Field.PRICE_LEVEL
        )
        val searchCenter = LatLng(lat, long)
        val circle = CircularBounds.newInstance(searchCenter, 50000.0)
        var includedTypes = listOf(
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

        if ((att != null)){
            includedTypes = listOf(att.value)
            binding.tvItemName.text = att.title
            binding.textView.text = att.desc
            binding.ivItemPhoto.setBackgroundResource(att.image)
        }

        if (text != null && text == "termurah")
        {
            binding.tvItemName.text = "Termurah"
            binding.textView.text = "Ingin Mencari Cafe Termurah? Ini Rekomendasinya"
            val searchNearbyRequest = searchnearby2(circle, placeFields, "Cafe dan Restorant Termurah")
            placesClient.searchByText(searchNearbyRequest)
                .addOnSuccessListener { response ->
                    val places = response.places
                    for (place in places) {
                        if (place.placeTypes != null) {
                            var photoUrl : PhotoMetadata? = null
                            if (!place.photoMetadatas.isNullOrEmpty())
                            {
                                photoUrl = place.photoMetadatas?.get(0)
                            }

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
                }
                .addOnFailureListener { exception: Exception ->
                    Log.e(TAG, "Place not found: ${exception.message}")
                }
        }else{
            val searchNearbyRequest = searchnearby(circle, placeFields, includedTypes, text)
            placesClient.searchNearby(searchNearbyRequest)
                .addOnSuccessListener { response ->
                    val places = response.places
                    for (place in places) {
                        if (place.placeTypes != null) {
                            var photoUrl : PhotoMetadata? = null
                            if (!place.photoMetadatas.isNullOrEmpty())
                            {
                                photoUrl = place.photoMetadatas?.get(0)
                            }

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
                }
                .addOnFailureListener { exception: Exception ->
                    Log.e(TAG, "Place not found: ${exception.message}")
                }
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
        if ((kriteria != null))
        {
            if (kriteria == "terdekat") {
                binding.tvItemName.text = "Terdekat"
                binding.textView.text = "Butuh Cafe Terdekat? Ini Rekomendasinya"
                searchNearbyRequest =
                    SearchNearbyRequest.builder(circle, placeFields)
                        .setIncludedTypes(includedTypes)
                        .setMaxResultCount(20)
                        .setRankPreference(SearchNearbyRequest.RankPreference.DISTANCE)
                        .build()
            }else if (kriteria.equals("terbaik")) {
            binding.tvItemName.text = "Rating Jempolan"
            binding.textView.text = "Butuh Cafe Dengan rating tertinggi? Ini Rekomendasinya"
            searchNearbyRequest =
                SearchNearbyRequest.builder(circle, placeFields)
                    .setIncludedTypes(includedTypes)
                    .setMaxResultCount(20)
                    .setRankPreference(SearchNearbyRequest.RankPreference.POPULARITY)
                    .build()
        }
        }
        return searchNearbyRequest
    }

    fun searchnearby2(
        circle: CircularBounds,
        placeFields: List<Place.Field>,
        text : String
    ): SearchByTextRequest {
        val searchByTextRequest: SearchByTextRequest =
            SearchByTextRequest.builder(text, placeFields)
                .setPriceLevels(listOf(1,2))
                .setLocationBias(circle)
                .setMaxResultCount(20)
                .build()
        return searchByTextRequest
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        const val LATITUDE = "lat"
        const val LONGTITUDE = "LONG"
        const val EXTRA_ATT = "attribute"
    }
}
