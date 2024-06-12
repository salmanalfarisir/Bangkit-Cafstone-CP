package com.cafstone.application.view.preferance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.cafstone.application.R
import com.cafstone.application.databinding.FragmentPreference1Binding

@Suppress("NAME_SHADOWING")
class PreferenceFragment1 : Fragment() {
    private var _binding: FragmentPreference1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPreference1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as? PreferenceActivity
        activity?.let { activity ->
            setPriceLevel(activity)
            setButton(activity.servesBeer, binding.servesBeer)
            setButton(activity.servesWine, binding.servesWine)
            setButton(activity.servesCocktails, binding.servesCockTail)
            setButton(activity.goodForChildren, binding.goodForChildren)
            setButton(activity.goodForGroups, binding.goodForGroups)
            setButton(activity.reservable, binding.reservable)
            setButton(activity.outdoorSeating, binding.outDoorSeating)
            setButton(activity.liveMusic, binding.liveMusic)
            setButton(activity.servesDessert, binding.servesDessert)

            binding.priceLevel1.setOnClickListener {
                activity.priceLevel = 1
                setPriceLevel(activity)
            }
            binding.priceLevel2.setOnClickListener {
                activity.priceLevel = 2
                setPriceLevel(activity)
            }
            binding.priceLevel3.setOnClickListener {
                activity.priceLevel = 3
                setPriceLevel(activity)
            }
            binding.priceLevel4.setOnClickListener {
                activity.priceLevel = 4
                setPriceLevel(activity)
            }

            binding.servesBeer.setOnClickListener {
                activity.servesBeer = setClick(activity.servesBeer)
                setButton(activity.servesBeer, binding.servesBeer)
            }

            binding.servesCockTail.setOnClickListener {
                activity.servesCocktails = setClick(activity.servesCocktails)
                setButton(activity.servesCocktails, binding.servesCockTail)
            }

            binding.servesWine.setOnClickListener {
                activity.servesWine = setClick(activity.servesWine)
                setButton(activity.servesWine, binding.servesWine)
            }

            binding.goodForChildren.setOnClickListener {
                activity.goodForChildren = setClick(activity.goodForChildren)
                setButton(activity.goodForChildren, binding.goodForChildren)
            }

            binding.goodForGroups.setOnClickListener {
                activity.goodForGroups = setClick(activity.goodForGroups)
                setButton(activity.goodForGroups, binding.goodForGroups)
            }

            binding.reservable.setOnClickListener {
                activity.reservable = setClick(activity.reservable)
                setButton(activity.reservable, binding.reservable)
            }

            binding.outDoorSeating.setOnClickListener {
                activity.outdoorSeating = setClick(activity.outdoorSeating)
                setButton(activity.outdoorSeating, binding.outDoorSeating)
            }

            binding.liveMusic.setOnClickListener {
                activity.liveMusic = setClick(activity.liveMusic)
                setButton(activity.liveMusic, binding.liveMusic)
            }

            binding.servesDessert.setOnClickListener {
                activity.servesDessert = setClick(activity.servesDessert)
                setButton(activity.servesDessert, binding.servesDessert)
            }
        }
    }

    fun setClick(status: Boolean): Boolean {
        return !status
    }

    fun setButton(status: Boolean, button: AppCompatButton) {
        if (status) {
            button.setBackgroundResource(R.drawable.button_for_reference_yes)
        } else {
            button.setBackgroundResource(R.drawable.button_for_reference_no)
        }
    }

    fun setPriceLevel(activity: PreferenceActivity) {
        when (activity.priceLevel) {
            1 -> {
                binding.priceLevel1.setBackgroundResource(R.drawable.button_for_reference_yes)
                binding.priceLevel2.setBackgroundResource(R.drawable.button_for_reference_no)
                binding.priceLevel3.setBackgroundResource(R.drawable.button_for_reference_no)
                binding.priceLevel4.setBackgroundResource(R.drawable.button_for_reference_no)
            }

            2 -> {
                binding.priceLevel1.setBackgroundResource(R.drawable.button_for_reference_no)
                binding.priceLevel2.setBackgroundResource(R.drawable.button_for_reference_yes)
                binding.priceLevel3.setBackgroundResource(R.drawable.button_for_reference_no)
                binding.priceLevel4.setBackgroundResource(R.drawable.button_for_reference_no)
            }

            3 -> {
                binding.priceLevel1.setBackgroundResource(R.drawable.button_for_reference_no)
                binding.priceLevel2.setBackgroundResource(R.drawable.button_for_reference_no)
                binding.priceLevel3.setBackgroundResource(R.drawable.button_for_reference_yes)
                binding.priceLevel4.setBackgroundResource(R.drawable.button_for_reference_no)
            }

            4 -> {
                binding.priceLevel1.setBackgroundResource(R.drawable.button_for_reference_no)
                binding.priceLevel2.setBackgroundResource(R.drawable.button_for_reference_no)
                binding.priceLevel3.setBackgroundResource(R.drawable.button_for_reference_no)
                binding.priceLevel4.setBackgroundResource(R.drawable.button_for_reference_yes)
            }

            else -> {
                binding.priceLevel1.setBackgroundResource(R.drawable.button_for_reference_no)
                binding.priceLevel2.setBackgroundResource(R.drawable.button_for_reference_no)
                binding.priceLevel3.setBackgroundResource(R.drawable.button_for_reference_no)
                binding.priceLevel4.setBackgroundResource(R.drawable.button_for_reference_no)
            }
        }
    }

}