package com.cafstone.application.view.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.cafstone.application.R
import com.cafstone.application.databinding.FragmentSearchEmptyBinding
import com.cafstone.application.view.nearby.NearbyActivity
import com.cafstone.application.view.nearby.NearbyModel


class SearchEmptyFragment : Fragment() {
    private var _binding: FragmentSearchEmptyBinding? = null
    private val binding get() = _binding!!
    private lateinit var button: List<CardView>
    private val title = listOf(
        "Indonesia Restaurant", "Chinese Restaurant", "American Restaurant",
        "Italian Restaurant", "Korea Restaurant", "Japanese Restaurant", "Thailand Restaurant",
        "India Restaurant", "Fast Food Restaurant", "Vegetarian Restaurant", "Steak House",
        "Coffee Shop"
    )
    private val desc = listOf(
        "Makanan Indonesia", "Makanan Cina", "Makanan Amerika",
        "Makanan Italia", "Makanan Korea", "Makanan Jepang", "Makanan Thailand",
        "Makanan India", "Makanan Cepat Saji", "Vegetarian Food", "Makanan yang Berbau Steak",
        "Tempat untuk bersantai seperti Coffee Shop"
    )
    private val image = listOf(
        R.drawable.indonesian_food,
        R.drawable.chinese_food,
        R.drawable.american_food,
        R.drawable.italian_food,
        R.drawable.korean_food,
        R.drawable.japanse_food,
        R.drawable.thai_food,
        R.drawable.indian_food,
        R.drawable.fast_food,
        R.drawable.vegetarian_food,
        R.drawable.steak_house,
        R.drawable.coffee_shop
    )
    private val isi = listOf(
        "indonesian_restaurant", "chinese_restaurant", "american_restaurant",
        "italian_restaurant", "korean_restaurant", "japanese_restaurant", "thai_restaurant",
        "indian_restaurant", "fast_food_restaurant", "vegetarian_restaurant", "steak_house",
        "coffee_shop"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchEmptyBinding.inflate(inflater, container, false)
        button = listOf(
            binding.indonesianFoodCard,
            binding.chineseFoodCard,
            binding.americanFoodCard,
            binding.italianFoodCard,
            binding.koreanFoodCard,
            binding.japaneseFoodCard,
            binding.thaiFoodCard,
            binding.indianFoodCard,
            binding.fastFoodCard,
            binding.vegetarianFood,
            binding.steakHouseFoodCard,
            binding.coffeeShopCard
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as? SearchViewActivity
        activity?.let { act ->
            button.forEachIndexed { index, item ->
                item.setOnClickListener {
                    val data = NearbyModel(
                        title[index],
                        "Ingin Mencoba ${desc[index]}? Berikut Rekomendasinya.",
                        image[index],
                        isi[index]
                    )
                    setButton(act, data)
                }
            }
        }
    }

    private fun setButton(activity: SearchViewActivity, data: NearbyModel) {
        val intent = Intent(requireActivity().applicationContext, NearbyActivity::class.java)
        intent.putExtra(NearbyActivity.EXTRA_ATT, data)
        intent.putExtra(NearbyActivity.LATITUDE, activity.lat)
        intent.putExtra(NearbyActivity.LONGTITUDE, activity.long)
        startActivity(intent)
    }
}