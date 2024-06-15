package com.cafstone.application.view.preferance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.cafstone.application.R
import com.cafstone.application.databinding.FragmentPreference2Binding

@Suppress("NAME_SHADOWING")
class PreferenceFragment2 : Fragment() {

    private var _binding: FragmentPreference2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPreference2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as? PreferenceActivity
        activity?.let { activity ->
            setButton(activity.acceptsCreditCards, binding.acceptsCreditCards)
            setButton(activity.acceptsDebitCards, binding.acceptsDeditCards)
            setButton(activity.acceptsCashOnly, binding.acceptsCashOnly)
            setButton(activity.acceptsNfc, binding.acceptsNfc)

            binding.acceptsCashOnly.setOnClickListener {
                activity.acceptsCashOnly = setClick(activity.acceptsCashOnly)
                setButton(activity.acceptsCashOnly, binding.acceptsCashOnly)
            }

            binding.acceptsDeditCards.setOnClickListener {
                activity.acceptsDebitCards = setClick(activity.acceptsDebitCards)
                setButton(activity.acceptsDebitCards, binding.acceptsDeditCards)
            }

            binding.acceptsCreditCards.setOnClickListener {
                activity.acceptsCreditCards = setClick(activity.acceptsCreditCards)
                setButton(activity.acceptsCreditCards, binding.acceptsCreditCards)
            }

            binding.acceptsNfc.setOnClickListener {
                activity.acceptsNfc = setClick(activity.acceptsNfc)
                setButton(activity.acceptsNfc, binding.acceptsNfc)
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
}