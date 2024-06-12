package com.cafstone.application.view.preferance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.cafstone.application.R
import com.cafstone.application.databinding.ActivityPreferanceBinding
import com.cafstone.application.databinding.ActivitySearchViewBinding
import com.cafstone.application.databinding.FragmentPreference1Binding
import com.cafstone.application.databinding.FragmentSearchBinding
import com.cafstone.application.view.search.SearchViewActivity

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
        activity?.let{activity->
            setpricelevel(activity)
            setbutton(activity.servesBeer,binding.servesbeer)
            setbutton(activity.servesWine,binding.serveswine)
            setbutton(activity.servesCocktails,binding.servescocktail)
            setbutton(activity.goodForChildren,binding.goodforchildren)
            setbutton(activity.goodForGroups,binding.goodforgroups)
            setbutton(activity.reservable,binding.reservable)
            setbutton(activity.outdoorSeating,binding.outdoorseating)
            setbutton(activity.liveMusic,binding.livemusic)
            setbutton(activity.servesDessert,binding.servesdessert)

            binding.priceLevel1.setOnClickListener {
                activity.priceLevel = 1
                setpricelevel(activity)
            }
            binding.priceLevel2.setOnClickListener {
                activity.priceLevel = 2
                setpricelevel(activity)
            }
            binding.priceLevel3.setOnClickListener {
                activity.priceLevel = 3
                setpricelevel(activity)
            }
            binding.priceLevel4.setOnClickListener {
                activity.priceLevel = 4
                setpricelevel(activity)
            }

            binding.servesbeer.setOnClickListener {
                activity.servesBeer = setclick(activity.servesBeer)
                setbutton(activity.servesBeer,binding.servesbeer)
            }

            binding.servescocktail.setOnClickListener {
                activity.servesCocktails = setclick(activity.servesCocktails)
                setbutton(activity.servesCocktails,binding.servescocktail)
            }

            binding.serveswine.setOnClickListener {
                activity.servesWine = setclick(activity.servesWine)
                setbutton(activity.servesWine,binding.serveswine)
            }

            binding.goodforchildren.setOnClickListener {
                activity.goodForChildren = setclick(activity.goodForChildren)
                setbutton(activity.goodForChildren,binding.goodforchildren)
            }

            binding.goodforgroups.setOnClickListener {
                activity.goodForGroups = setclick(activity.goodForGroups)
                setbutton(activity.goodForGroups,binding.goodforgroups)
            }

            binding.reservable.setOnClickListener {
                activity.reservable = setclick(activity.reservable)
                setbutton(activity.reservable,binding.reservable)
            }

            binding.outdoorseating.setOnClickListener {
                activity.outdoorSeating = setclick(activity.outdoorSeating)
                setbutton(activity.outdoorSeating,binding.outdoorseating)
            }

            binding.livemusic.setOnClickListener {
                activity.liveMusic = setclick(activity.liveMusic)
                setbutton(activity.liveMusic,binding.livemusic)
            }

            binding.servesdessert.setOnClickListener {
                activity.servesDessert = setclick(activity.servesDessert)
                setbutton(activity.servesDessert,binding.servesdessert)
            }
        }
    }

    fun setclick(status : Boolean) : Boolean{
            if(status){
                return false
            }else{
                return true
            }
    }

    fun setbutton(status : Boolean,Button : Button)
    {
        if(status)
        {
            Button.setBackgroundResource(R.color.primary_color)
        }else{
            Button.setBackgroundResource(R.color.button_no)
        }
    }
    fun setpricelevel(activity : PreferenceActivity){
        if (activity.priceLevel == 1){
            binding.priceLevel1.setBackgroundResource(R.color.primary_color)
            binding.priceLevel2.setBackgroundResource(R.color.button_no)
            binding.priceLevel3.setBackgroundResource(R.color.button_no)
            binding.priceLevel4.setBackgroundResource(R.color.button_no)
        }else if (activity.priceLevel == 2){
            binding.priceLevel1.setBackgroundResource(R.color.button_no)
            binding.priceLevel2.setBackgroundResource(R.color.primary_color)
            binding.priceLevel3.setBackgroundResource(R.color.button_no)
            binding.priceLevel4.setBackgroundResource(R.color.button_no)
        }else if (activity.priceLevel == 3){
            binding.priceLevel1.setBackgroundResource(R.color.button_no)
            binding.priceLevel2.setBackgroundResource(R.color.button_no)
            binding.priceLevel3.setBackgroundResource(R.color.primary_color)
            binding.priceLevel4.setBackgroundResource(R.color.button_no)
        }
        else if (activity.priceLevel == 4){
            binding.priceLevel1.setBackgroundResource(R.color.button_no)
            binding.priceLevel2.setBackgroundResource(R.color.button_no)
            binding.priceLevel3.setBackgroundResource(R.color.button_no)
            binding.priceLevel4.setBackgroundResource(R.color.primary_color)
        }else{
            binding.priceLevel1.setBackgroundResource(R.color.button_no)
            binding.priceLevel2.setBackgroundResource(R.color.button_no)
            binding.priceLevel3.setBackgroundResource(R.color.button_no)
            binding.priceLevel4.setBackgroundResource(R.color.button_no)
        }
    }

}