package com.cafstone.application.view.search

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.cafstone.application.R
import com.cafstone.application.data.adapter.AdapterModel
import com.cafstone.application.data.adapter.PlacesAdapter
import com.cafstone.application.databinding.ActivitySearchViewBinding
import com.cafstone.application.di.PlacesClientSingleton
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.google.android.libraries.places.api.net.SearchByTextResponse
import java.util.Locale

@Suppress("DEPRECATION")
class SearchViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchViewBinding
    var result = ""
    lateinit var placesClient: PlacesClient
    val fragmentManager = supportFragmentManager
    private val searchFragment = SearchFragment()
    val emptyFragment = SearchEmptyFragment()
    private val placesList = mutableListOf<AdapterModel>()
    private lateinit var adapter: PlacesAdapter
    var lat: Double = 0.0
    var long: Double = 0.0


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE
        lat = intent.getDoubleExtra(LATITUDE, 0.0)
        long = intent.getDoubleExtra(LONGITUDE, 0.0)

        if (lat != 0.0 && long != 0.0) {
            adapter = PlacesAdapter(placesList)
            val fragment =
                fragmentManager.findFragmentByTag(SearchEmptyFragment::class.java.simpleName)
            if (fragment !is SearchEmptyFragment) {
                Log.d(
                    "MyFlexibleFragment",
                    "Fragment Name :" + SearchEmptyFragment::class.java.simpleName
                )
                fragmentManager
                    .beginTransaction()
                    .add(
                        R.id.fragmentContainer,
                        emptyFragment,
                        SearchEmptyFragment::class.java.simpleName
                    )
                    .commit()
            }

            binding.backButton.setOnClickListener {
                finish()
            }

            placesClient = PlacesClientSingleton.getInstance(this)

            binding.searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrEmpty() || newText.length < 3) {
                        if (fragment !is SearchEmptyFragment) {
                            fragmentManager
                                .beginTransaction()
                                .replace(
                                    R.id.fragmentContainer,
                                    emptyFragment,
                                    SearchEmptyFragment::class.java.simpleName
                                )
                                .commit()
                        }
                    } else {
                        val lowerText = newText.lowercase(Locale.getDefault())
                        result = lowerText
                        searchText(lowerText)
                    }
                    return true
                }
            })
        } else {
            finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                statusBarColor = Color.TRANSPARENT
            }
        }
        supportActionBar?.hide()
    }


    fun searchText(text: String) {
        showLoading(true)
        placesList.clear()
        // Create a new PlacesClient instance

        // Specify the list of fields to return
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.TYPES,
            Place.Field.PHOTO_METADATAS,
            Place.Field.RATING
        )


        val searchCenter = LatLng(lat, long)
        // Define latitude and longitude coordinates of the search area

        // Use the builder to create a SearchByTextRequest object
        val searchByTextRequest: SearchByTextRequest =
            SearchByTextRequest.builder(text, placeFields)
                .setMaxResultCount(20)
                .setLocationBias(CircularBounds.newInstance(searchCenter, 0.0)).build()

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

        // Call PlacesClient.searchByText() to perform the search
        placesClient.searchByText(searchByTextRequest)
            .addOnSuccessListener { response: SearchByTextResponse ->
                val places = response.places
                for (place in places) {
                    if (place.placeTypes != null) {
                        var i = 0
                        place.placeTypes?.forEach {
                            if (it in includedTypes) {
                                i += 1
                            }
                        }
                        if (i != 0) {
                            var photoUrl: PhotoMetadata? = null
                            if (!place.photoMetadatas.isNullOrEmpty()) {
                                photoUrl = place.photoMetadatas?.get(0)
                            }

                            Log.d(TAG, "0")
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
                }
                val fragments =
                    fragmentManager.findFragmentByTag(SearchFragment::class.java.simpleName)
                if (fragments is SearchFragment) {
                    searchFragment.updateSearchResults(placesList)
                } else {
                    Log.d(TAG, "Search Empty Fragment")
                    fragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.fragmentContainer,
                            searchFragment,
                            SearchFragment::class.java.simpleName
                        )
                        .commit()
                }
            }
            .addOnFailureListener { exception: Exception ->
                Log.e(TAG, "Place not found: ${exception.message}")
            }
        showLoading(false)
    }

    fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val LATITUDE = "lat"
        const val LONGITUDE = "long"
    }
}