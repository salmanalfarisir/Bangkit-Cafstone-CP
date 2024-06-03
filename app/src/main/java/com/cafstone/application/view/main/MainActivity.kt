package com.cafstone.application.view.main

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafstone.application.BuildConfig
import com.cafstone.application.data.adapter.AdapterModel
import com.cafstone.application.data.adapter.PlacesAdapter
import com.cafstone.application.databinding.ActivityMainBinding
import com.cafstone.application.view.ViewModelFactory
import com.cafstone.application.view.onboarding.FirstOBActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.google.android.libraries.places.api.net.SearchByTextResponse

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private val placesList = mutableListOf<AdapterModel>()
    private lateinit var adapter: PlacesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, FirstOBActivity::class.java))
                finish()
            }
        }

        binding.rvReview.layoutManager = LinearLayoutManager(this)
        adapter = PlacesAdapter(placesList)
        binding.rvReview.adapter = adapter

        // Initialize the SDK
        searchText("Cafe Viral Jakarta")
        setupView()
        setupAction()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun searchText(text: String) {
        if (!Places.isInitialized()) {
            Log.d(TAG, "Token : " + BuildConfig.MAPS_API_KEY)
            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        }

        // Create a new PlacesClient instance
        val placesClient: PlacesClient = Places.createClient(this)

        // Specify the list of fields to return
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.PHOTO_METADATAS
        )

        // Define latitude and longitude coordinates of the search area
        val searchCenter = LatLng(3.5629935, 98.6529746)

        // Use the builder to create a SearchByTextRequest object
        val searchByTextRequest: SearchByTextRequest =
            SearchByTextRequest.builder(text, placeFields)
                .setLocationBias(CircularBounds.newInstance(searchCenter, 0.0)).build()

        // Call PlacesClient.searchByText() to perform the search
        placesClient.searchByText(searchByTextRequest)
            .addOnSuccessListener { response: SearchByTextResponse ->
                val places = response.places
                for (place in places) {
                    val photoUrl = place.photoMetadatas?.firstOrNull()?.let { photoMetadata ->
                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&photoreference=${photoMetadata.zzb()}&key=${BuildConfig.BASE_URL}"
                    }
                    if (photoUrl != null) {
                        Log.d(TAG,photoUrl)
                    }
                    placesList.add(
                        AdapterModel(
                            place.id!!,
                            place.name!!,
                            place.address!!,
                            photoUrl
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception: Exception ->
                Log.e(TAG, "Place not found: ${exception.message}")
            }
    }

    fun fetchPhotoUrl(placesClient: PlacesClient, photoMetadata: PhotoMetadata): Bitmap? {
        val photoRequest = FetchPhotoRequest.builder(photoMetadata)
            .setMaxWidth(200) // Set the desired width
            .setMaxHeight(180) // Set the desired height
            .build()
        var photoBitmap: Bitmap? = null
        placesClient.fetchPhoto(photoRequest).addOnSuccessListener { fetchPhotoResponse ->
            photoBitmap = fetchPhotoResponse.bitmap
        }.addOnFailureListener { exception ->
            Log.e("Photo Fetch Error", "Error fetching photo: ${exception.message}")
        }
        return photoBitmap
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
        }
    }
}