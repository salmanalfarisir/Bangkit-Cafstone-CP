package com.cafstone.application.view.preferance

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.cafstone.application.R
import com.cafstone.application.databinding.FragmentPreference1Binding
import com.cafstone.application.databinding.FragmentPreference2Binding

class PreferenceFragment2 : Fragment() {

    private var _binding: FragmentPreference2Binding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPreference2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as? PreferenceActivity
        activity?.let{activity->
            setbutton(activity.acceptsCreditCards,binding.acceptsCreditCards)
            setbutton(activity.acceptsDebitCards,binding.acceptsDeditCards)
            setbutton(activity.acceptsCashOnly,binding.acceptsCashOnly)
            setbutton(activity.acceptsNfc,binding.acceptsNfc)

            binding.acceptsCashOnly.setOnClickListener {
                activity.acceptsCashOnly = setclick(activity.acceptsCashOnly)
                setbutton(activity.acceptsCashOnly,binding.acceptsCashOnly)
            }

            binding.acceptsDeditCards.setOnClickListener {
                activity.acceptsDebitCards = setclick(activity.acceptsDebitCards)
                setbutton(activity.acceptsDebitCards,binding.acceptsDeditCards)
            }

            binding.acceptsCreditCards.setOnClickListener {
                activity.acceptsCreditCards = setclick(activity.acceptsCreditCards)
                setbutton(activity.acceptsCreditCards,binding.acceptsCreditCards)
            }

            binding.acceptsNfc.setOnClickListener {
                activity.acceptsNfc = setclick(activity.acceptsNfc)
                setbutton(activity.acceptsNfc,binding.acceptsNfc)
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
}