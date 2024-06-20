package com.cafstone.application.view.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import com.bumptech.glide.Glide
import com.cafstone.application.data.adapter.AdapterModel
import com.cafstone.application.data.adapter.PlacesAdapter3
import com.cafstone.application.data.pref.UserModel
import com.cafstone.application.databinding.ActivityMainBinding
import com.cafstone.application.di.PlacesClientSingleton
import com.cafstone.application.view.ViewModelFactory
import com.cafstone.application.view.nearby.NearbyActivity
import com.cafstone.application.view.onboardingpage.OnboardingActivity
import com.cafstone.application.view.profile.ProfileActivity
import com.cafstone.application.view.search.SearchViewActivity
import com.cafstone.application.view.welcome.SplashScreenActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.google.android.libraries.places.api.net.SearchByTextResponse
import java.util.Locale


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
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
    private lateinit var binding: ActivityMainBinding
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private var currentLocation: Location? = null

    private val placesList = mutableListOf<AdapterModel>()
    private val placesList1 = mutableListOf<AdapterModel>()
    private val placesList2 = mutableListOf<AdapterModel>()
    private val placesList3 = mutableListOf<AdapterModel>()

    private lateinit var adapter: PlacesAdapter3
    private lateinit var adapter1: PlacesAdapter3
    private lateinit var adapter2: PlacesAdapter3
    private lateinit var adapter3: PlacesAdapter3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        binding.profileCard.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, OnboardingActivity::class.java))
                finish()
            } else {
                placesClient = PlacesClientSingleton.getInstance(this)
                locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                getMyLastLocation()
                setupView()
            }
        }
    }


    //LOKASI START
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }

                else -> {
                    // No location access granted.
                    AlertDialog.Builder(this).apply {
                        setTitle(title)
                        setMessage("The Permission Denied, Please Acc that")
                        setPositiveButton("OK") { _, _ ->
                            getMyLastLocation()
                        }
                        create()
                        show()
                    }
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (!isGpsEnabled) {
                AlertDialog.Builder(this).apply {
                    setTitle(title)
                    setMessage("Mohon Hidupkan GPS anda")
                    setPositiveButton("OK") { _, _ ->
                        getMyLastLocation()
                    }
                    create()
                    show()
                }
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        currentLocation = location
                        setButton(location)
                        setupAction()
                    } else {
                        AlertDialog.Builder(this).apply {
                            setTitle(title)
                            setMessage("Gagal Mengambil Lokasi Coba Kembali")
                            setPositiveButton("OK") { _, _ ->
                                val intent =
                                    Intent(this@MainActivity, SplashScreenActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                            create()
                            show()
                        }
                    }
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
//LOKASI END


    private fun getCityNameFromCoordinates(latitude: Double, longitude: Double): String? {
        val geocoder = Geocoder(this.applicationContext, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        return if (!addresses.isNullOrEmpty()) {
            addresses[0].locality // This gives you the city name
        } else {
            null
        }
    }

    //SEARCH TEXT
    @SuppressLint("NotifyDataSetChanged")
    fun searchText(text: String, p: Int, a: Int) {
        // Create a new PlacesClient instance
        // Specify the list of fields to return
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.TYPES,
            Place.Field.PHOTO_METADATAS,
            Place.Field.RATING,
            Place.Field.USER_RATINGS_TOTAL,
            Place.Field.PRIMARY_TYPE,
            Place.Field.LAT_LNG
        )

        // Define latitude and longitude coordinates of the search area
        val lat = currentLocation?.latitude ?: 3.5629935
        val long = currentLocation?.longitude ?: 98.6529746
        val searchCenter = LatLng(lat, long)

        // Use the builder to create a SearchByTextRequest object
        val searchByTextRequest: SearchByTextRequest =
            SearchByTextRequest.builder(text, placeFields)
                .setMinRating(4.0)
                .setLocationBias(CircularBounds.newInstance(searchCenter, 5000.0)).build()

        // Call PlacesClient.searchByText() to perform the search
        placesClient.searchByText(searchByTextRequest)
            .addOnSuccessListener { response: SearchByTextResponse ->
                val places = response.places
                var tipe = ""
                for (place in places) {
                    if (place.placeTypes != null) {
                        var i = 0
                        place.placeTypes?.forEach {
                            val index = includedTypes.indexOf(it)
                            if (index != -1)
                            {
                                i += 1
                                if (tipe == "")
                                {
                                    tipe = nameType[index]
                                }
                            }
                        }

                        val index = includedTypes.indexOf(place.primaryType?:"")
                        if (index != -1)
                        {
                            tipe = nameType[index]
                        }
                        if (i != 0) {
                            var photoUrl: PhotoMetadata? = null
                            if (!place.photoMetadatas.isNullOrEmpty()) {
                                photoUrl = place.photoMetadatas?.get(0)
                            }
                            when (p) {
                                0 -> {
                                    placesList.add(
                                        AdapterModel(
                                            place.id!!,
                                            place.name!!,
                                            tipe,
                                            place.userRatingsTotal?.toString() ?: "0",
                                            photoUrl,
                                            currentLocation?.latitude ?: 3.5629935,
                                            currentLocation?.longitude ?: 98.6529746,
                                            place.latLng?.latitude?:0.0,
                                            place.latLng?.longitude?:0.0,
                                            place.rating
                                        )
                                    )
                                }

                                1 -> {
                                    placesList1.add(
                                        AdapterModel(
                                            place.id!!,
                                            place.name!!,
                                            tipe,
                                            place.userRatingsTotal?.toString() ?: "0",
                                            photoUrl,
                                            currentLocation?.latitude ?: 3.5629935,
                                            currentLocation?.longitude ?: 98.6529746,
                                            place.latLng?.latitude?:0.0,
                                            place.latLng?.longitude?:0.0,
                                            place.rating
                                        )
                                    )
                                }

                                2 -> {
                                    placesList2.add(
                                        AdapterModel(
                                            place.id!!,
                                            place.name!!,
                                            tipe,
                                            place.userRatingsTotal?.toString() ?: "0",
                                            photoUrl,
                                            currentLocation?.latitude ?: 3.5629935,
                                            currentLocation?.longitude ?: 98.6529746,
                                            place.latLng?.latitude?:0.0,
                                            place.latLng?.longitude?:0.0,
                                            place.rating
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                when (a) {
                    0 -> {

                        adapter.notifyDataSetChanged()
                    }

                    1 -> {

                        adapter1.notifyDataSetChanged()
                    }

                    2 -> {
                        adapter2.notifyDataSetChanged()
                    }
                }
            }
            .addOnFailureListener { exception: Exception ->
                Log.e(TAG, "Place not found: ${exception.message}")
                when (p) {
                    0 -> {
                        binding.textView.visibility = View.GONE
                        binding.rvReview.visibility = View.GONE
                    }

                    1 -> {
                        binding.textView1.visibility = View.GONE
                        binding.rvReview1.visibility = View.GONE
                    }

                    2 -> {
                        binding.textView2.visibility = View.GONE
                        binding.rvReview2.visibility = View.GONE
                    }
                }
            }
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars())
            supportActionBar?.hide()
        }
    }


    private fun setupAction() {
        currentLocation?.let { location ->
            Log.d(TAG, "Lokasi : " + location.latitude + " + " + location.longitude)
            binding.btnLocation.text =
                getCityNameFromCoordinates(location.latitude, location.longitude)
            binding.rvReview.layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
            binding.rvReview1.layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
            binding.rvReview2.layoutManager = LinearLayoutManager(this, HORIZONTAL, false)
            binding.rvReview3.layoutManager = LinearLayoutManager(this, HORIZONTAL, false)

            adapter = PlacesAdapter3(placesList)
            binding.rvReview.adapter = adapter
            adapter1 = PlacesAdapter3(placesList1)
            binding.rvReview1.adapter = adapter1
            adapter2 = PlacesAdapter3(placesList2)
            binding.rvReview2.adapter = adapter2
            adapter3 = PlacesAdapter3(placesList3)
            binding.rvReview3.adapter = adapter3

            val data = viewModel.getData()
            binding.displayName.text = data.name
            binding.displayGreeting.text = data.welcome
            getRecommendation(data)
            viewModel.isLoading.observe(this) { it ->
                showLoading(it)
                if (!it) {
                    viewModel.recommendation.observe(this) {
                        if (it == null) {
                            binding.textView3.visibility = View.GONE
                            binding.rvReview3.visibility = View.GONE
                        } else {
                            val fields = listOf(
                                Place.Field.ID,
                                Place.Field.NAME,
                                Place.Field.ADDRESS,
                                Place.Field.RATING,
                                Place.Field.PHOTO_METADATAS,
                                Place.Field.TYPES,
                                Place.Field.USER_RATINGS_TOTAL,
                                Place.Field.PRIMARY_TYPE,
                                Place.Field.LAT_LNG
                            )
                            it.forEach { data ->
                                val placeRequest = FetchPlaceRequest.newInstance(data.id, fields)
                                placesClient.fetchPlace(placeRequest)
                                    .addOnSuccessListener { response ->
                                        val place = response.place
                                        var tipe = ""
                                        if (!place.placeTypes.isNullOrEmpty()){
                                            for(p in place.placeTypes!!){
                                                val index = includedTypes.indexOf(p)
                                                if (index != -1)
                                                {
                                                    tipe = nameType[index]
                                                    break
                                                }
                                            }
                                        }
                                        val index = includedTypes.indexOf(place.primaryType?:"")
                                        if (index != -1)
                                        {
                                            tipe = nameType[index]
                                        }
                                        var photoUrl: PhotoMetadata? = null
                                        if (!place.photoMetadatas.isNullOrEmpty()) {
                                            photoUrl = place.photoMetadatas?.get(0)
                                        }
                                        placesList3.add(
                                            AdapterModel(
                                                place.id!!,
                                                place.name!!,
                                                tipe,
                                                place.userRatingsTotal?.toString() ?: "0",
                                                photoUrl,
                                                location.latitude,
                                                location.longitude,
                                                place.latLng?.latitude?:0.0,
                                                place.latLng?.longitude?:0.0,
                                                place.rating
                                            )
                                        )
                                        Log.d("Main", "Data Diupdate")
                                        adapter3.notifyItemInserted(placesList3.size - 1)
                                    }.addOnFailureListener { exception ->
                                        if (exception is ApiException) {
                                            Log.e(
                                                "MainActivity",
                                                "Gagal: ${exception.message}"
                                            )
                                        }
                                    }
                            }
                        }
                    }
                }
            }
            searchText("Cafe Yang Sedang Banyak Dikunjungi", 1, 1)
            searchText("Cafe Fancy", 2, 2)
            searchText("Cafe Ternyaman", 0, 0)

            Glide.with(this)
                .load("https://cdn.idntimes.com/content-images/post/20240207/33bac083ba44f180c1435fc41975bf36-ca73ec342155d955387493c4eb78c8bb.jpg")
                .into(binding.photoprofil)
        }
    }

    private fun setButton(location: Location) {
        binding.searchBar.setOnClickListener {
            val intent = Intent(this, SearchViewActivity::class.java).apply {
                putExtra(SearchViewActivity.LATITUDE, location.latitude)
                putExtra(SearchViewActivity.LONGITUDE, location.longitude)
            }
            startActivity(intent)
        }
        binding.nearByCardView.setOnClickListener {
            val intent = Intent(this, NearbyActivity::class.java)
            intent.putExtra(NearbyActivity.EXTRA_DETAIL, "terdekat")
            intent.putExtra(NearbyActivity.LATITUDE, location.latitude)
            intent.putExtra(NearbyActivity.LONGITUDE, location.longitude)
            startActivity(intent)
        }
        binding.ratingCardView.setOnClickListener {
            val intent = Intent(this, NearbyActivity::class.java)
            intent.putExtra(NearbyActivity.EXTRA_DETAIL, "terbaik")
            intent.putExtra(NearbyActivity.LATITUDE, location.latitude)
            intent.putExtra(NearbyActivity.LONGITUDE, location.longitude)
            startActivity(intent)
        }
        binding.priceCardView.setOnClickListener {
            val intent = Intent(this, NearbyActivity::class.java)
            intent.putExtra(NearbyActivity.EXTRA_DETAIL, "termurah")
            intent.putExtra(NearbyActivity.LATITUDE, location.latitude)
            intent.putExtra(NearbyActivity.LONGITUDE, location.longitude)
            startActivity(intent)
        }
        binding.btnLocation.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle(title)
                setMessage("Ambil Lokasi?")
                setPositiveButton("OK") { _, _ ->
                    getMyLastLocation()
                }
                create()
                show()
            }
        }
    }

    private fun showLoading(bool: Boolean) {
        binding.progressBar.visibility = if (bool) View.VISIBLE else View.GONE
    }

    private fun getRecommendation(user: UserModel) {
        val list = RecommendationModel(
            listOf(
                setBoolean(user.servesBeer),
                setBoolean(user.servesWine),
                setBoolean(user.servesCocktails),
                setBoolean(user.goodForChildren),
                setBoolean(user.goodForGroups),
                setBoolean(user.reservable),
                setBoolean(user.outdoorSeating),
                setBoolean(user.liveMusic),
                setBoolean(user.servesDessert),
                user.priceLevel,
                setBoolean(user.acceptsCreditCards),
                setBoolean(user.acceptsDebitCards),
                setBoolean(user.acceptsCashOnly),
                setBoolean(user.acceptsNfc)
            )
        )
        viewModel.recommendation(list)
    }

    private fun setBoolean(boolean: Boolean): Int {
        return if (boolean) {
            1
        } else {
            0
        }
    }


}