package com.cafstone.application.view.detail

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.cafstone.application.R
import com.cafstone.application.data.adapter.ImageAdapter
import com.cafstone.application.databinding.ActivityDetailBinding
import com.cafstone.application.di.PlacesClientSingleton
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest
import com.google.android.libraries.places.api.net.PlacesClient


class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var placesClient: PlacesClient
    private lateinit var photoAdapter: ImageAdapter
    private val photoUris = mutableListOf<Uri>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = ViewPagerAdapter(this)

        val id = intent.getStringExtra(PLACE_ID)
        if (id != null){
            binding.rvReview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            photoAdapter = ImageAdapter(photoUris)
            binding.rvReview.adapter = photoAdapter

            // Create a new PlacesClient instance
            placesClient = PlacesClientSingleton.getInstance(this)

            fetchPlacePhotos(id)
        }else{
            finish()
        }
    }

    private fun fetchPlacePhotos(placeId: String) {
        val fields = listOf(Place.Field.ID,Place.Field.NAME,Place.Field.RATING,Place.Field.REVIEWS,Place.Field.PHOTO_METADATAS)
        val placeRequest = FetchPlaceRequest.newInstance(placeId, fields)

        placesClient.fetchPlace(placeRequest).addOnSuccessListener { response ->
            val place = response.place
            val metadata = place.photoMetadatas
            if (metadata.isNullOrEmpty()) {
                Log.w("MainActivity", "No photo metadata.")
                return@addOnSuccessListener
            }
            for (photoMetadata in metadata) {
                val photoRequest = FetchResolvedPhotoUriRequest.builder(photoMetadata)
                    .setMaxWidth(300)
                    .setMaxHeight(280)
                    .build()

                placesClient.fetchResolvedPhotoUri(photoRequest).addOnSuccessListener { fetchResolvedPhotoUriResponse ->
                    val uri = fetchResolvedPhotoUriResponse.uri
                    uri?.let { photoUris.add(it) }
                    photoAdapter.notifyItemInserted(photoUris.size - 1)
                }.addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        Log.e("MainActivity", "Place not found: ${exception.message}")
                    }
                }
            }
        }.addOnFailureListener { exception ->
            if (exception is ApiException) {
                Log.e("MainActivity", "Place not found: ${exception.message}")
            }
        }
    }
    companion object{
        const val PLACE_ID = "place_id"
    }
}