package com.cafstone.application.view.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafstone.application.data.adapter.RingkasanJamAdapter
import com.cafstone.application.databinding.FragmentRingkasanBinding
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class RingkasanFragment : Fragment() {
    private lateinit var binding : FragmentRingkasanBinding
    private val list = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRingkasanBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val act = requireActivity() as DetailActivity
        act.let {
            if(it.ringkasan[0].toString() == ""){
                binding.listAddress.visibility = View.GONE
            }else{
                binding.addressDescription.text = it.ringkasan[0].toString()
            }
            list.clear()
            if(it.openlist.isEmpty())
            {
                val today = LocalDate.now().dayOfWeek
                binding.hari.text = today.toString()
                list.add("Buka 24 Jam")

            }else{
                val today = LocalDate.now().dayOfWeek
                binding.hari.text = today.toString()
                it.openlist.forEach {open->
                    val time = open.open?.time
                    val close = open.close?.time
                    if (time != null && close != null) {
                        val openTime = LocalTime.of(time.hours, time.minutes)
                        val closeTime = LocalTime.of(close.hours, close.minutes)
                        val formatter = DateTimeFormatter.ofPattern("HH.mm")
                        val formattedTime = openTime.format(formatter)
                        val formatters = DateTimeFormatter.ofPattern("HH.mm")
                        val formattedTimes = closeTime.format(formatters)
                        val jam = "$formattedTime - $formattedTimes"
                        list.add(jam)
                    }
                }
            }
            binding.rvjam.layoutManager = LinearLayoutManager(context)
            val adapter = RingkasanJamAdapter(list)
            binding.rvjam.adapter = adapter

            if(it.ringkasan[1].toString() == ""){
                binding.listNoTelp.visibility = View.GONE
            }else{
                binding.noTelp.text = it.ringkasan[1].toString()
            }
            if (it.ringkasan[2] == false || it.ringkasan[2] == "")
            {
                binding.listReserve.visibility = View.GONE
            }else{
                binding.reservable.text = "Reservable"
            }

            if (it.ringkasan[3] == ""){
                binding.listMenu.visibility = View.GONE
            }else{
                binding.linkMenu.setOnClickListener{button->
                    val url = it.ringkasan[3].toString()
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
                binding.linkMenu.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }
}