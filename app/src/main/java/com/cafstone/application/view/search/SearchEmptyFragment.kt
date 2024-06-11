package com.cafstone.application.view.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cafstone.application.databinding.FragmentSearchEmptyBinding
import com.cafstone.application.view.Nearby.NearbyActivity


class SearchEmptyFragment : Fragment() {
    private var _binding: FragmentSearchEmptyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchEmptyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logoutButton.setOnClickListener{
            val activity = requireActivity() as? SearchViewActivity
            activity?.let {
                val intent = Intent(requireActivity().applicationContext,NearbyActivity::class.java)
                intent.putExtra(NearbyActivity.EXTRA_DETAIL, "terdekat")
                intent.putExtra(NearbyActivity.LATITUDE,activity.lat)
                intent.putExtra(NearbyActivity.LONGTITUDE, activity.long)
                startActivity(intent)
            }
        }
    }
}