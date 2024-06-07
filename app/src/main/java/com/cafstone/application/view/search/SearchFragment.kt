package com.cafstone.application.view.search

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafstone.application.BuildConfig
import com.cafstone.application.data.adapter.AdapterModel
import com.cafstone.application.data.adapter.PlacesAdapter
import com.cafstone.application.databinding.FragmentSearchBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.google.android.libraries.places.api.net.SearchByTextResponse


class SearchFragment : Fragment() {

    private lateinit var searchAdapter: PlacesAdapter
    private val placesList = mutableListOf<AdapterModel>()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragmen
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchAdapter = PlacesAdapter(placesList)
        binding.rvReview.layoutManager = LinearLayoutManager(context)
        binding.rvReview.adapter = searchAdapter
        val activity = requireActivity() as? SearchViewActivity
        activity?.let{
            searchText(it,it.s)
        }
    }
    fun updateSearchResults(newPlacesList: List<AdapterModel>) {
        placesList.clear()
        placesList.addAll(newPlacesList)
        searchAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun searchText(activity: SearchViewActivity, text: String) {
        activity.showloading(true)
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
        val currentLocation = activity.currentLocation
        // Define latitude and longitude coordinates of the search area
        val lat = currentLocation?.latitude ?: 3.5629935
        val long = currentLocation?.longitude ?: 98.6529746
        val searchCenter = LatLng(lat,long)
        // Use the builder to create a SearchByTextRequest object
        val searchByTextRequest: SearchByTextRequest =
            SearchByTextRequest.builder(text, placeFields)
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
        activity.placesClient.searchByText(searchByTextRequest)
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
                                Log.d(ContentValues.TAG, photoUrl)
                            }

                            Log.d(ContentValues.TAG,"0")
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
                searchAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception: Exception ->
                Log.e(ContentValues.TAG, "Place not found: ${exception.message}")
            }
        activity.showloading(false)
    }
}