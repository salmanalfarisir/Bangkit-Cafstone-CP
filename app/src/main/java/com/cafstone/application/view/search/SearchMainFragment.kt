package com.cafstone.application.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.cafstone.application.R
import com.cafstone.application.data.adapter.SearchAdapter
import com.cafstone.application.data.adapter.SearchModel
import com.cafstone.application.databinding.FragmentSearchMainBinding

class SearchMainFragment : Fragment() {
    private var _binding: FragmentSearchMainBinding? = null
    private val binding get() = _binding!!
    private val list = mutableListOf<SearchModel>()
    private lateinit var searchAdapter: SearchAdapter
    private val title = listOf(
        "Indonesia Food", "Chinese Food", "American Food",
        "Italian Food", "Korea Food", "Japanese Food", "Thailand Food",
        "India Food", "Fast Food Food", "Vegetarian Food", "Steak House",
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
        _binding = FragmentSearchMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as? SearchViewActivity
        activity?.let { act ->
            title.forEachIndexed { index, data ->
                list.add(
                    SearchModel(
                        isi[index],
                        title[index],
                        desc[index],
                        act.lat,
                        act.long,
                        image[index]
                    )
                )
            }
            binding.rvReview.layoutManager = GridLayoutManager(context, 3)
            searchAdapter = SearchAdapter(list)
            binding.rvReview.adapter = searchAdapter
        }

    }
}