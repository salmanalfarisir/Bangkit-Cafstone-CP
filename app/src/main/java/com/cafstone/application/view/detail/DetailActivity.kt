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
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
            Place.Field.ADDRESS,
            Place.Field.CURRENT_OPENING_HOURS,
            Place.Field.RATING,
            Place.Field.REVIEWS,
            Place.Field.PHOTO_METADATAS,
            Place.Field.SECONDARY_OPENING_HOURS,
            Place.Field.USER_RATINGS_TOTAL,
            Place.Field.TYPES
        )

        val placeRequest = FetchPlaceRequest.newInstance(placeId, fields)

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

        //Tambahin type sesuai dengan urutan list diatas

        val nameType: List<String> = listOf(
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


        placesClient.fetchPlace(placeRequest).addOnSuccessListener { response ->
            val place = response.place

            binding.ratingtextview.text = place.rating?.toString() ?: "0.0"
            binding.reviewcounttextview.text = place.userRatingsTotal?.toString() ?: "0"
            binding.titleDestination.text = place.name?.toString() ?: ""
            val type = place.placeTypes
            binding.tipeRestoran.text = ""
            var tipe = ""
            if (type != null) {
                type.forEach {
                    if (it in includedTypes) {
                        if (tipe == "") {
                            tipe = it
                        } else {
                            tipe += ",$it"
                        }
                    }
                }
                binding.tipeRestoran.text = tipe
            } else {
                binding.tipeRestoran.visibility = View.GONE
            }
            val openingHours = place.openingHours?.periods
            var isi = 0
            if (openingHours != null) {
                for (open in openingHours) {
                    val timeOfWeek = open.open?.day
                    if (timeOfWeek != null) {
                        val today = LocalDate.now().dayOfWeek
                        if (timeOfWeek.toString() == today.toString()) {
                            val time = open.open?.time
                            val close = open.close?.time
                            if (time != null && close != null) {
                                val openTime = LocalTime.of(time.hours, time.minutes)
                                val closeTime = LocalTime.of(close.hours, close.minutes)
                                val currentTime = LocalTime.now()
                                val opens = currentTime.isAfter(openTime)
                                val closed = currentTime.isBefore(closeTime)
                                if (opens && closed) {
                                    binding.statusDescription.text = getString(R.string.place_buka)
                                    binding.statusDescription.setTextColor(Color.GREEN)
                                } else {
                                    if (closeTime.isBefore(openTime)) {
                                        if (currentTime.isAfter(openTime) || currentTime.isBefore(
                                                closeTime
                                            )
                                        ) {
                                            binding.statusDescription.text =
                                                getString(R.string.place_buka)
                                            binding.statusDescription.setTextColor(Color.GREEN)
                                        } else {
                                            binding.statusDescription.text =
                                                getString(R.string.place_tutup)
                                            binding.statusDescription.setTextColor(Color.RED)
                                        }
                                    } else {
                                        binding.statusDescription.text =
                                            getString(R.string.place_tutup)
                                        binding.statusDescription.setTextColor(Color.RED)
                                    }
                                }
                                val formatter = DateTimeFormatter.ofPattern("HH.mm")
                                val formattedTime = openTime.format(formatter)
                                binding.timeDescription.text = formattedTime
                                isi = 1
                                break
                            }
                        }
                    }
                }
            }
            if (isi == 0) {
                binding.statusDescription.text = getString(R.string.place_buka)
                binding.statusDescription.setTextColor(Color.GREEN)
                binding.timeDescription.text = getString(R.string.place_buka_24_jam)
            }
            binding.addressDescription.text = place.address
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