package com.cafstone.application.view.nearby

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.cafstone.application.R
import com.cafstone.application.databinding.FragmentFilterBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val nameType: List<String> = listOf(
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
    private val includedTypes = listOf(
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

    val pricelevel = listOf("Murah", "Sedang", "Lumayan", "Mahal")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("DiscouragedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                    dismiss()
//                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Logika tambahan saat geser, jika diperlukan

            }
        })
        val act = requireActivity() as NearbyActivity
        act.let { activity ->
            binding.button.isEnabled = true
            binding.checkboxContainer.let {
                for (i in 1..includedTypes.size) {
                    val checkBox = CheckBox(context)
                    val checkBoxId = includedTypes[i - 1]
                    val resId = resources.getIdentifier(checkBoxId, "id", context?.packageName)
                    checkBox.id = resId
                    checkBox.text = nameType[i - 1]
                    it.addView(checkBox)

                    if (activity.types.contains(checkBoxId)) {
                        checkBox.isChecked = true
                    }
                    // Add OnCheckedChangeListener to CheckBox
                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            // Add the CheckBox ID to the list if checked
                            activity.types.add(checkBoxId)
                        } else {
                            // Remove the CheckBox ID from the list if unchecked
                            activity.types.remove(checkBoxId)
                        }
                    }
                }
            }
            binding.pricelevel.let {
                for (i in 1..pricelevel.size) {
                    val checkBox = CheckBox(context)
                    val checkBoxId = "$i"
                    val resId = resources.getIdentifier(checkBoxId, "id", context?.packageName)
                    checkBox.id = resId
                    checkBox.text = pricelevel[i - 1]
                    it.addView(checkBox)

                    if (activity.pricelevel.contains(i)) {
                        checkBox.isChecked = true
                    }
                    // Add OnCheckedChangeListener to CheckBox
                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            // Add the CheckBox ID to the list if checked
                            activity.pricelevel.add(i)
                        } else {
                            // Remove the CheckBox ID from the list if unchecked
                            activity.pricelevel.remove(i)
                        }
                    }
                }
            }

            binding.button.setOnClickListener {
                activity.searchAgain()
                dismiss()
            }

            if (activity.data != null) {
                binding.lokasi.visibility = View.GONE
            }
            if (activity.att != null) {
                binding.checkboxparent.visibility = View.GONE
            }
            setButton(activity.datafragment)
            binding.terdekat.setOnClickListener {
                if (activity.datafragment == "terdekat") {
                    activity.datafragment = null
                } else {
                    activity.datafragment = "terdekat"
                }
                setButton(activity.datafragment)
            }
            binding.terpopuler.setOnClickListener {
                if (activity.datafragment == "terbaik") {
                    activity.datafragment = null
                } else {
                    activity.datafragment = "terbaik"
                }
                setButton(activity.datafragment)
            }
        }
    }

    private fun setButton(status: String?) {
        if (status != null) {
            if (status == "terdekat") {
                binding.terdekat.setBackgroundResource(R.drawable.button_for_reference_yes)
                binding.terpopuler.setBackgroundResource(R.drawable.button_for_reference_no)
            } else {
                binding.terdekat.setBackgroundResource(R.drawable.button_for_reference_no)
                binding.terpopuler.setBackgroundResource(R.drawable.button_for_reference_yes)
            }
        } else {
            binding.terdekat.setBackgroundResource(R.drawable.button_for_reference_no)
            binding.terpopuler.setBackgroundResource(R.drawable.button_for_reference_no)
        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            // Set the height to 3/4 of the screen height
            val layoutParams = it.layoutParams
            layoutParams.height = (resources.displayMetrics.heightPixels * 0.80).toInt()
            it.layoutParams = layoutParams
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isHideable = false

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    // Handle state changes here
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // Handle slide offset here
                    if (slideOffset < -0.5) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            })
        }
    }
}