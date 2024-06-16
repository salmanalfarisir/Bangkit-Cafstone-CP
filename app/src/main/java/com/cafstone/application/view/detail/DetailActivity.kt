package com.cafstone.application.view.detail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cafstone.application.R
import com.cafstone.application.data.adapter.ImageAdapter
import com.cafstone.application.databinding.ActivityDetailBinding
import com.cafstone.application.di.PlacesClientSingleton
import com.cafstone.application.view.main.MainActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var placesClient: PlacesClient
    private lateinit var photoAdapter: ImageAdapter
    private val photoUris = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.a.title.text = ""
        binding.a.toolbar.setBackgroundColor(Color.TRANSPARENT)

        binding.a.buttonToolbar.visibility = View.GONE
        binding.a.backButton.setOnClickListener {
            startActivity(Intent(this@DetailActivity, MainActivity::class.java))
        }


        binding.a.toolbar.apply {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.title = ""
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra(PLACE_ID)
        if (id != null) {
            photoAdapter = ImageAdapter(photoUris)
            binding.imageContainer.adapter = photoAdapter

            // Create a new PlacesClient instance
            placesClient = PlacesClientSingleton.getInstance(this)
            TabLayoutMediator(binding.tabLayout, binding.imageContainer) { _, _ ->
            }.attach()
            binding.viewPager.adapter = DetailSectionPager(this)
            TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            fetchPlacePhotos(id)
            binding.nextButton.setOnClickListener {
                if (binding.imageContainer.currentItem < binding.imageContainer.adapter!!.itemCount - 1) {
                    binding.imageContainer.currentItem += 1
                }
            }
            binding.prevButton.setOnClickListener {
                if (binding.imageContainer.currentItem != 0) {
                    binding.imageContainer.currentItem -= 1
                }
            }
        } else {
            finish()
        }
    }


    private fun fetchPlacePhotos(placeId: String) {
        val fields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.OPENING_HOURS,
            Place.Field.CURRENT_OPENING_HOURS,
            Place.Field.RATING,
            Place.Field.REVIEWS,
            Place.Field.PHOTO_METADATAS,
            Place.Field.SECONDARY_OPENING_HOURS,
            Place.Field.TYPES
        )

        val placeRequest = FetchPlaceRequest.newInstance(placeId, fields)

        placesClient.fetchPlace(placeRequest).addOnSuccessListener { response ->
            val place = response.place
            val metadata = place.photoMetadatas
            if (metadata.isNullOrEmpty()) {
                Log.w("MainActivity", "No photo metadata.")
                return@addOnSuccessListener
            }

            binding.ratingtextview.text = place.rating?.toString() ?: "0.0"

            val review = place.reviews
            if (review != null) {
                binding.reviewcounttextview.text = place.reviews?.size.toString()
            } else {
                binding.reviewcounttextview.text = "0"
            }
            binding.titleDestination.text = place.name?.toString() ?: ""
            val type = place.placeTypes
            if (type != null) {
                binding.tipeRestoran.text = type.joinToString(separator = ", ")
            } else {
                binding.tipeRestoran.visibility = View.GONE
            }

            for (photoMetadata in metadata) {
                val photoRequest = FetchResolvedPhotoUriRequest.builder(photoMetadata)
                    .setMaxWidth(300)
                    .setMaxHeight(280)
                    .build()

                placesClient.fetchResolvedPhotoUri(photoRequest)
                    .addOnSuccessListener { fetchResolvedPhotoUriResponse ->
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

    companion object {
        const val PLACE_ID = "place_id"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3
        )
    }
}