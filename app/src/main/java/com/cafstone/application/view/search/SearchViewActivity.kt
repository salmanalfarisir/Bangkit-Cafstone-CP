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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cafstone.application.R
import com.cafstone.application.data.adapter.AdapterModel
import com.cafstone.application.data.adapter.PlacesAdapter
import com.cafstone.application.databinding.ActivitySearchViewBinding
import com.cafstone.application.di.PlacesClientSingleton
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.Locale

@Suppress("DEPRECATION")
class SearchViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchViewBinding
    var result = ""
    lateinit var placesClient: PlacesClient
    val fragmentManager = supportFragmentManager
    private val searchFragment = SearchFragment()
    val emptyFragment = SearchMainFragment()
    private val placesList = mutableListOf<AdapterModel>()
    private lateinit var adapter: PlacesAdapter
    var lat: Double = 0.0
    var long: Double = 0.0


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.progressBar.visibility = View.GONE
        lat = intent.getDoubleExtra(LATITUDE, 0.0)
        long = intent.getDoubleExtra(LONGITUDE, 0.0)

        if (lat != 0.0 && long != 0.0) {
            adapter = PlacesAdapter(placesList)
            val fragment =
                fragmentManager.findFragmentByTag(SearchMainFragment::class.java.simpleName)
            if (fragment !is SearchMainFragment) {
                Log.d(
                    "MyFlexibleFragment",
                    "Fragment Name :" + SearchMainFragment::class.java.simpleName
                )
                fragmentManager
                    .beginTransaction()
                    .add(
                        R.id.fragmentContainer,
                        emptyFragment,
                        SearchMainFragment::class.java.simpleName
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
                        if (fragment !is SearchMainFragment) {
                            fragmentManager
                                .beginTransaction()
                                .replace(
                                    R.id.fragmentContainer,
                                    emptyFragment,
                                    SearchMainFragment::class.java.simpleName
                                )
                                .commit()
                        }
                    } else {
                        val lowerText = newText.lowercase(Locale.getDefault())
                        result = lowerText
                        searchText2(lowerText)
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

    fun searchText2(text: String) {
        val fragments =
            fragmentManager.findFragmentByTag(SearchFragment::class.java.simpleName)
        if (fragments is SearchFragment) {
            searchFragment.searchText(this, text)
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