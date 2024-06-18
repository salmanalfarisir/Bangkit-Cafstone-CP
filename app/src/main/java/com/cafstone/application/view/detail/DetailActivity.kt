package com.cafstone.application.view.detail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cafstone.application.R
import com.cafstone.application.data.adapter.ImageAdapter
import com.cafstone.application.databinding.ActivityDetailBinding
import com.cafstone.application.di.PlacesClientSingleton
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.Period
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
    val openlist = mutableListOf<Period>()
    lateinit var ringkasan : List<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.a.title.text = ""
        binding.a.toolbar.setBackgroundColor(Color.TRANSPARENT)

        binding.a.buttonToolbar.visibility = View.GONE
        binding.a.backButton.setOnClickListener {
            finish()
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
        val lat = intent.getDoubleExtra(lat,0.0)
        val long = intent.getDoubleExtra(long,0.0)
        if (id != null && lat != 0.0 && long != 0.0) {
            photoAdapter = ImageAdapter(photoUris)
            binding.imageContainer.adapter = photoAdapter

            // Create a new PlacesClient instance
            placesClient = PlacesClientSingleton.getInstance(this)
            TabLayoutMediator(binding.tabLayout, binding.imageContainer) { _, _ ->
            }.attach()
            fetchPlacePhotos(id,lat,long)
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


    private fun fetchPlacePhotos(placeId: String, lat : Double,long: Double) {
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
            Place.Field.PHONE_NUMBER,
            Place.Field.RESERVABLE,
            Place.Field.LAT_LNG,
            Place.Field.WEBSITE_URI,
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
            val latlang = place.latLng
            binding.navigate.setOnClickListener{
                AlertDialog.Builder(this).apply {
                    setTitle(title)
                    setMessage("Ingin Menavigasikan Ke Alamatnya?")
                    setPositiveButton("Iya") { _, _ ->
                        val uri =
                            "http://maps.google.com/maps?saddr=$lat,$long&daddr=${latlang.latitude},${latlang.longitude}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        intent.setPackage("com.google.android.apps.maps")
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        }
                    }
                    setNegativeButton("Tidak"){dialog,_->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
            }

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
            binding.statusDescription.text = openingHours.toString()
            openlist.clear()
            if (openingHours != null) {
                for (open in openingHours) {
                    val timeOfWeek = open.open?.day
                    if (timeOfWeek != null) {
                        val today = LocalDate.now().dayOfWeek
                        if (timeOfWeek.toString() == today.toString()) {
                            openlist.add(open)
                        }
                    }
                }
            }
            var isi = 0
            if (openlist.isEmpty())
            {
                binding.statusDescription.text = getString(R.string.place_buka)
                binding.statusDescription.setTextColor(Color.GREEN)
                binding.openorclosed.visibility = View.GONE
                binding.timeDescription.text = getString(R.string.place_buka_24_jam)
            }else{
                openlist.forEachIndexed{index,open->
                    val time = open.open?.time
                    val close = open.close?.time
                    if (time != null && close != null) {
                        val openTime = LocalTime.of(time.hours, time.minutes)
                        val closeTime = LocalTime.of(close.hours, close.minutes)
                        val currentTime = LocalTime.of(12,0)
                        val opens = currentTime.isAfter(openTime)
                        val closed = currentTime.isBefore(closeTime)
                        if (isi != 1)
                        {
                            if (opens && closed) {
                                binding.statusDescription.text = getString(R.string.place_buka)
                                binding.statusDescription.setTextColor(Color.GREEN)
                                val formatter = DateTimeFormatter.ofPattern("HH.mm")
                                val formattedTime = closeTime.format(formatter)
                                binding.timeDescription.text = formattedTime
                                binding.openorclosed.text = "Tutup Pukul"
                                isi = 1
                            } else {
                                if (closeTime.isBefore(openTime)) {
                                    if ((currentTime.isAfter(openTime) && currentTime.isBefore(LocalTime.of(23,59))) ||
                                        ((currentTime == LocalTime.of(0,0) ||currentTime.isAfter(LocalTime.of(0,0))) && currentTime.isBefore(closeTime))
                                    ) {
                                        binding.statusDescription.text =
                                            getString(R.string.place_buka)
                                        binding.statusDescription.setTextColor(Color.GREEN)
                                        val formatter = DateTimeFormatter.ofPattern("HH.mm")
                                        val formattedTime = closeTime.format(formatter)
                                        binding.timeDescription.text = formattedTime
                                        binding.openorclosed.text = "Tutup Pukul"
                                        isi = 1
                                    } else {
                                        binding.statusDescription.text =
                                            getString(R.string.place_tutup)
                                        binding.statusDescription.setTextColor(Color.RED)
                                        val formatter = DateTimeFormatter.ofPattern("HH.mm")
                                        val formattedTime = openTime.format(formatter)
                                        binding.timeDescription.text = formattedTime
                                        binding.openorclosed.text = "Buka Pukul"
                                        isi = 1
                                    }
                                } else if (index == openlist.size-1){
                                    binding.statusDescription.text =
                                        getString(R.string.place_tutup)
                                    binding.statusDescription.setTextColor(Color.RED)
                                    val formatter = DateTimeFormatter.ofPattern("HH.mm")
                                    val formattedTime = openTime.format(formatter)
                                    binding.timeDescription.text = formattedTime
                                    binding.openorclosed.text = "Buka Pukul"
                                }
                            }
                        }
                    }
                }
            }
            val address = place.address ?: ""
            val nohp = place.phoneNumber?: ""
            val res = place.reservable ?: ""
            val web = place.websiteUri ?: ""
            ringkasan = listOf(address,nohp,res,web)
            binding.viewPager.adapter = DetailSectionPager(this)
            TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
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
        const val lat = "latitude"
        const val long = "longtitude"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_3
        )
    }
}