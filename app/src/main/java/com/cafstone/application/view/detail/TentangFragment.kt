package com.cafstone.application.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.cafstone.application.data.adapter.TentangAdapter
import com.cafstone.application.databinding.FragmentTentangBinding
import com.google.android.libraries.places.api.model.Place

class TentangFragment : Fragment() {

    private lateinit var binding : FragmentTentangBinding
    private val service = mutableListOf<String>()
    private val offering = mutableListOf<String>()
    private val access = mutableListOf<String>()
    private val dining = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTentangBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val act = requireActivity() as DetailActivity
        act.let {
            service.clear()
            if(it.service.isNotEmpty())
            {
                it.service.forEachIndexed { index, booleanPlaceAttributeValue ->
                    if(booleanPlaceAttributeValue == Place.BooleanPlaceAttributeValue.TRUE)
                    {
                        service.add(it.servicename[index])
                    }
                }
                if (service.isNotEmpty())
                {
                    binding.rvPenawaran.layoutManager = GridLayoutManager(context,2)
                    val adapter = TentangAdapter(service)
                    binding.rvPenawaran.adapter = adapter
                }else{
                    binding.layoutPenawaran.visibility = View.GONE
                    binding.materialDivider.visibility = View.GONE
                }
            }


            access.clear()
            if(it.accessibility.isNotEmpty())
            {
                it.accessibility.forEachIndexed { index, booleanPlaceAttributeValue ->
                    if(booleanPlaceAttributeValue == Place.BooleanPlaceAttributeValue.TRUE)
                    {
                        access.add(it.accessibilityname[index])
                    }
                }
                if (access.isNotEmpty())
                {
                    binding.rvFasilitas.layoutManager = GridLayoutManager(context,2)
                    val adapter = TentangAdapter(access)
                    binding.rvFasilitas.adapter = adapter
                }else{
                    binding.layoutFasilitas.visibility = View.GONE
                    binding.materialDivider2.visibility = View.GONE
                }
            }


            offering.clear()
            if(it.offerings.isNotEmpty())
            {
                it.offerings.forEachIndexed { index, booleanPlaceAttributeValue ->
                    if(booleanPlaceAttributeValue == Place.BooleanPlaceAttributeValue.TRUE)
                    {
                        offering.add(it.offeringsname[index])
                    }
                }
                if (offering.isNotEmpty())
                {
                    binding.rvPilihanMakanan.layoutManager = GridLayoutManager(context,2)
                    val adapter = TentangAdapter(offering)
                    binding.rvPilihanMakanan.adapter = adapter
                }else{
                    binding.layoutPersediaanMakanan.visibility = View.GONE
                    binding.materialDivider3.visibility = View.GONE
                }
            }
            dining.clear()
            if(it.diningoption.isNotEmpty())
            {
                it.diningoption.forEachIndexed { index, booleanPlaceAttributeValue ->
                    if(booleanPlaceAttributeValue == Place.BooleanPlaceAttributeValue.TRUE)
                    {
                        dining.add(it.diningoptionname[index])
                    }
                }
                if (dining.isNotEmpty())
                {
                    binding.rvOpsiLayanan.layoutManager = GridLayoutManager(context,2)
                    val adapter = TentangAdapter(dining)
                    binding.rvOpsiLayanan.adapter = adapter
                }else{
                    binding.layoutOpsiLayanan.visibility = View.GONE
                    binding.materialDivider6.visibility = View.GONE
                }
            }
        }
    }
}