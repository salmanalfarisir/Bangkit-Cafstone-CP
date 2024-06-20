@file:Suppress("DEPRECATION")

package com.cafstone.application.view.nearby

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafstone.application.R
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
    var pricelevel = mutableListOf<Int>()
    var data: String? = null
    var datafragment: String? = null
    var lat: Double = 0.0
    private var long: Double = 0.0
    var att: NearbyModel? = null
    var types = mutableListOf<String>()
    private var includedTypes = listOf(
        "restaurant",
        "american_restaurant",
        "bar",
        "sandwich_shop",
        "coffee_shop",
        "fast_food_restaurant",
        "seafood_restaurant",
        "steak_house",
        "sushi_restaurant",
        "vegetarian_restaurant",
        "ice_cream_shop",
        "japanese_restaurant",
        "korean_restaurant",
        "brazilian_restaurant",
        "mexican_restaurant",
        "breakfast_restaurant",
        "middle_eastern_restaurant",
        "brunch_restaurant",
        "pizza_restaurant",
        "cafe",
        "ramen_restaurant",
        "chinese_restaurant",
        "mediterranean_restaurant",
        "meal_delivery",
        "meal_takeaway",
        "barbecue_restaurant",
        "spanish_restaurant",
        "greek_restaurant",
        "hamburger_restaurant",
        "thai_restaurant",
        "indian_restaurant",
        "turkish_restaurant",
        "indonesian_restaurant",
        "vegan_restaurant",
        "italian_restaurant"
    )

    private val nameType: List<String> = listOf(
        "Restaurant", "American Restaurant", "Bar", "Sandwich Shop", "Coffee Shop",
        "Fast Food Restaurant", "Seafood Restaurant", "Steak House", "Sushi Restaurant",
        "Vegetarian Restaurant", "Ice Cream Shop", "Japanese Restaurant", "Korean Restaurant",
        "Brazilian Restaurant", "Mexican Restaurant", "Breakfast Restaurant",
        "Middle Eastern Restaurant", "Brunch Restaurant", "Pizza Restaurant", "Cafe",
        "Ramen Restaurant", "Chinese Restaurant", "Mediterranean Restaurant", "Meal Delivery",
        "Meal Takeaway", "Barbecue Restaurant", "Spanish Restaurant", "Greek Restaurant",
        "Hamburger Restaurant", "Thai Restaurant", "Indian Restaurant", "Turkish Restaurant",
        "Indonesian Restaurant", "Vegan Restaurant", "Italian Restaurant"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNearbyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        adapter = PlacesAdapter(placesList)
        lat = intent.getDoubleExtra(LATITUDE, 0.0)
        long = intent.getDoubleExtra(LONGITUDE, 0.0)
        binding.rvReview.layoutManager = LinearLayoutManager(this)
        binding.rvReview.adapter = adapter
        if (lat != 0.0 && long != 0.0) {
            data = intent.getStringExtra(EXTRA_DETAIL)
            att = if (Build.VERSION.SDK_INT >= 33) {
                intent.getParcelableExtra(EXTRA_ATT, NearbyModel::class.java)
            } else {
                @Suppress("DEPRECATION") intent.getParcelableExtra(EXTRA_ATT)
            }
            if (att != null) {
                types.add(att!!.value)
            }
            nearby(data, att, lat, long)
            binding.filterButton.setOnClickListener {
                val a = FilterFragment()
                a.show(supportFragmentManager, a.tag)
            }
        } else {
            finish()
        }

    }

    fun searchAgain() {
        if (data != null) {
            nearby(data, att, lat, long)
        } else {
            nearby(datafragment, att, lat, long)
        }
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    fun nearby(text: String?, att: NearbyModel?, lat: Double, long: Double) {
        placesList.clear()
        placesClient = PlacesClientSingleton.getInstance(this)

        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.TYPES,
            Place.Field.PHOTO_METADATAS,
            Place.Field.RATING,
            Place.Field.PRICE_LEVEL,
            Place.Field.USER_RATINGS_TOTAL,
            Place.Field.PRIMARY_TYPE,
            Place.Field.PRICE_LEVEL,
            Place.Field.LAT_LNG
        )
        val searchCenter = LatLng(lat, long)
        val circle = CircularBounds.newInstance(searchCenter, 50000.0)

        if ((att != null)) {
            binding.placeName.text = att.title
            binding.ivItemPhoto.setBackgroundResource(att.image)
        }
        var inc = includedTypes
        if (types.isNotEmpty()) {
            inc = types
        }

        if (text != null && text == "termurah") {
            binding.placeName.text = getString(R.string.place_termurah)
            val judul = "Tempat Makan"
            val searchNearbyRequest =
                searchNearby2(circle, placeFields, "$judul dengan harga paling murah")
            placesClient.searchByText(searchNearbyRequest).addOnSuccessListener { response ->
                val places = response.places
                for (place in places) {
                    if (types.isNotEmpty()) {
                        var t = false
                        place.placeTypes?.forEach {
                            if (includedTypes.contains(it)) {
                                t = true
                            }
                        }
                        if (!t) {
                            continue
                        }
                    }
                    if (place.placeTypes != null) {
                        var tipe = ""
                        place.placeTypes?.forEach {
                            val index = includedTypes.indexOf(it)
                            if (index != -1) {
                                if (tipe == "") {
                                    tipe = nameType[index]
                                }
                            }
                        }

                        val index = includedTypes.indexOf(place.primaryType ?: "")
                        if (index != -1) {
                            tipe = nameType[index]
                        }

                        var photoUrl: PhotoMetadata? = null
                        if (!place.photoMetadatas.isNullOrEmpty()) {
                            photoUrl = place.photoMetadatas?.get(0)
                        }

                        placesList.add(
                            AdapterModel(
                                place.id!!,
                                place.name!!,
                                tipe,
                                place.userRatingsTotal?.toString() ?: "0.0",
                                photoUrl,
                                lat,
                                long,
                                place.latLng?.latitude ?: 0.0,
                                place.latLng?.longitude ?: 0.0,
                                place.rating
                            )
                        )
                    }
                }
                adapter.notifyDataSetChanged()
                binding.countPlaceName.text = placesList.size.toString() + " Restaurant"
            }.addOnFailureListener { exception: Exception ->
                Log.e(TAG, "Place not found: ${exception.message}")
            }
        } else {
            val searchNearbyRequest = searchNearby(circle, placeFields, inc, text)
            placesClient.searchNearby(searchNearbyRequest).addOnSuccessListener { response ->
                val places = response.places
                for (place in places) {
                    if (pricelevel.isNotEmpty()) {
                        var stop = 0
                        place.priceLevel?.let {
                            if (!pricelevel.contains(it)) {
                                stop = 1
                            }
                        }
                        if (stop == 1) {
                            continue
                        }
                    }
                    if (place.placeTypes != null) {
                        var tipe = ""
                        place.placeTypes?.forEach {
                            val index = includedTypes.indexOf(it)
                            if (index != -1) {
                                if (tipe == "") {
                                    tipe = nameType[index]
                                }
                            }
                        }

                        val index = includedTypes.indexOf(place.primaryType ?: "")
                        if (index != -1) {
                            tipe = nameType[index]
                        }
                        var photoUrl: PhotoMetadata? = null
                        if (!place.photoMetadatas.isNullOrEmpty()) {
                            photoUrl = place.photoMetadatas?.get(0)
                        }

                        placesList.add(
                            AdapterModel(
                                place.id!!,
                                place.name!!,
                                tipe,
                                place.userRatingsTotal?.toString() ?: "0.0",
                                photoUrl,
                                lat, long,
                                place.latLng?.latitude ?: 0.0,
                                place.latLng?.longitude ?: 0.0,
                                place.rating
                            )
                        )
                    }
                }
                adapter.notifyDataSetChanged()
                binding.countPlaceName.text = placesList.size.toString() + " Restaurant"
            }.addOnFailureListener { exception: Exception ->
                Log.e(TAG, "Place not found: ${exception.message}")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun searchNearby(
        circle: CircularBounds,
        placeFields: List<Place.Field>,
        includedTypes: List<String>,
        kriteria: String?
    ): SearchNearbyRequest {
        var searchNearbyRequest: SearchNearbyRequest =
            SearchNearbyRequest.builder(circle, placeFields).setIncludedTypes(includedTypes)
                .setMaxResultCount(20).build()
        if ((kriteria != null)) {
            if (kriteria == "terdekat") {
                binding.placeName.text = getString(R.string.place_terdekat)
                searchNearbyRequest =
                    SearchNearbyRequest.builder(circle, placeFields).setIncludedTypes(includedTypes)
                        .setMaxResultCount(20)

                        .setRankPreference(SearchNearbyRequest.RankPreference.DISTANCE).build()
            } else if (kriteria == "terbaik") {
                binding.placeName.text = getString(R.string.rating_jempolan)
                searchNearbyRequest =
                    SearchNearbyRequest.builder(circle, placeFields).setIncludedTypes(includedTypes)
                        .setMaxResultCount(20)
                        .setRankPreference(SearchNearbyRequest.RankPreference.POPULARITY).build()
            }
        }
        return searchNearbyRequest
    }

    @Suppress("SameParameterValue")
    private fun searchNearby2(
        circle: CircularBounds, placeFields: List<Place.Field>, text: String
    ): SearchByTextRequest {
        var searchByTextRequest: SearchByTextRequest =
            SearchByTextRequest.builder(text, placeFields).setPriceLevels(listOf(1, 2))
                .setLocationBias(circle)
                .setMaxResultCount(20)
                .build()
        if (pricelevel.isNotEmpty()) {
            searchByTextRequest =
                SearchByTextRequest.builder(text, placeFields).setPriceLevels(listOf(1, 2))
                    .setLocationBias(circle)
                    .setMaxResultCount(20)
                    .setPriceLevels(pricelevel)
                    .build()
        }
        return searchByTextRequest
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        const val LATITUDE = "lat"
        const val LONGITUDE = "LONG"
        const val EXTRA_ATT = "attribute"
    }
}