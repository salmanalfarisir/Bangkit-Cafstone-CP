package com.cafstone.application.view.search

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import com.cafstone.application.BuildConfig
import com.cafstone.application.R
import com.cafstone.application.data.adapter.AdapterModel
import com.cafstone.application.data.adapter.PlacesAdapter
import com.cafstone.application.databinding.ActivitySearchViewBinding
import com.cafstone.application.di.LocationSharedPreferences
import com.cafstone.application.di.PlacesClientSingleton
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.google.android.libraries.places.api.net.SearchByTextResponse
import java.util.Locale

class SearchViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchViewBinding
    var s = ""
    lateinit var placesClient: PlacesClient
    val fragmentManager = supportFragmentManager
    private val searchFragment = SearchFragment()
    val emptyFragment = SearchEmptyFragment()
    private val placesList = mutableListOf<AdapterModel>()
    private lateinit var adapter: PlacesAdapter
    var currentLocation: Location? = null


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE
        val location = LocationSharedPreferences.getLocation(this)
        if (location != null)
        {
            currentLocation = location
            adapter = PlacesAdapter(placesList)
            val fragment = fragmentManager.findFragmentByTag(SearchEmptyFragment::class.java.simpleName)
            if (fragment !is SearchEmptyFragment) {
                Log.d(
                    "MyFlexibleFragment",
                    "Fragment Name :" + SearchEmptyFragment::class.java.simpleName
                )
                fragmentManager
                    .beginTransaction()
                    .add(R.id.frame_container, emptyFragment, SearchEmptyFragment::class.java.simpleName)
                    .commit()
            }

            binding.backButton.setOnClickListener {
                finish()
            }

            if (!Places.isInitialized()) {
                Log.d(TAG, "Token : " + BuildConfig.MAPS_API_KEY)
                Places.initialize(this.applicationContext, BuildConfig.MAPS_API_KEY)
            }
            placesClient = PlacesClientSingleton.getInstance(this)

            binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // Handle the query submission here
                    // Example: perform a search with the query
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // Handle the text change here
                    // Example: update search suggestions
                    if (newText.isNullOrEmpty() || newText.length < 3)
                    {
                        if(fragment !is SearchEmptyFragment)
                        {
                            fragmentManager
                                .beginTransaction()
                                .replace(R.id.frame_container, emptyFragment, SearchEmptyFragment::class.java.simpleName)
                                .commit()
                        }
                    }
                    else{
                        val lowertext = newText.lowercase(Locale.getDefault())
                        s = lowertext
                        searchText(lowertext)
                    }
                    return true
                }
            })
        }else{
            finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars())
        }
        supportActionBar?.hide()
    }


    fun searchText(text: String) {
        showloading(true)
        placesList.clear()
        // Create a new PlacesClient instance

        // Specify the list of fields to return
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.TYPES,
            Place.Field.PHOTO_METADATAS
        )

        val lat = currentLocation?.latitude ?: 3.5629935
        val long = currentLocation?.longitude ?: 98.6529746
        val searchCenter = LatLng(lat,long)
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
                            val photoUrl = place.photoMetadatas?.firstOrNull()?.let { photoMetadata ->
                                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&photoreference=${photoMetadata.zzb()}&key=${BuildConfig.BASE_URL}"
                            }
                            if (photoUrl != null) {
                                Log.d(TAG, photoUrl)
                            }

                            Log.d(TAG,"0")
                            placesList.add(
                                AdapterModel(
                                    place.id!!,
                                    place.name!!,
                                    place.address!!,
                                    photoUrl
                                )
                            )
                        }
                    }
                }
                val fragments = fragmentManager.findFragmentByTag(SearchFragment::class.java.simpleName)
                if (fragments is SearchFragment)
                {
                    searchFragment.updateSearchResults(placesList)
                }else{
                    Log.d(TAG,"masuk awal")
                    fragmentManager
                        .beginTransaction()
                        .replace(R.id.frame_container, searchFragment, SearchFragment::class.java.simpleName)
                        .commit()
                }
            }
            .addOnFailureListener { exception: Exception ->
                Log.e(TAG, "Place not found: ${exception.message}")
            }
        showloading(false)
    }

    fun showloading (loading : Boolean){
        if (loading){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

}