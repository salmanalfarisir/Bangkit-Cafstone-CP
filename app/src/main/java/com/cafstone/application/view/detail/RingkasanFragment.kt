package com.cafstone.application.view.detail

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cafstone.application.data.adapter.AdapterModel
import com.cafstone.application.data.adapter.PlacesAdapter2
import com.cafstone.application.data.adapter.RingkasanJamAdapter
import com.cafstone.application.databinding.FragmentRingkasanBinding
import com.cafstone.application.view.main.NonSwipeableViewPager
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class RingkasanFragment : Fragment() {
    private lateinit var binding : FragmentRingkasanBinding
    private val list = mutableListOf<String>()
    private val list2 = mutableListOf<AdapterModel>()
    private lateinit var adapters : PlacesAdapter2
    private lateinit var view : View

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
        this.view = view
        view.post {
            (view.parent as? NonSwipeableViewPager)?.measuredWidthAndState
        }
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

            if (it.ringkasan[4] == ""){
                binding.listPlusCode.visibility = View.GONE
            }else{
                binding.plusCode.text=it.ringkasan[4].toString()
            }
            binding.recomended.text = "Tempat ini juga recommended loh"
            binding.rvPenelusuranOrang.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapters = PlacesAdapter2(list2)
            binding.rvPenelusuranOrang.adapter=adapters
            nearby(it,it.latt,it.longg)

        }
    }

    fun nearby(activity: DetailActivity, lat: Double, long: Double) {
        list2.clear()

        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.TYPES,
            Place.Field.PHOTO_METADATAS,
            Place.Field.RATING,
            Place.Field.PRICE_LEVEL,
            Place.Field.USER_RATINGS_TOTAL
        )
        val searchCenter = LatLng(lat, long)
        val circle = CircularBounds.newInstance(searchCenter, 50000.0)


            val searchNearbyRequest = SearchNearbyRequest.builder(circle, placeFields).setIncludedTypes(activity.types)
                .setMaxResultCount(10)
                .setRankPreference(SearchNearbyRequest.RankPreference.POPULARITY).
                build()
            activity.placesClient.searchNearby(searchNearbyRequest).addOnSuccessListener { response ->
                val places = response.places
                for (place in places) {
                    if (place.placeTypes != null) {
                        var photoUrl: PhotoMetadata? = null
                        if (!place.photoMetadatas.isNullOrEmpty()) {
                            photoUrl = place.photoMetadatas?.get(0)
                        }
                        val index = activity.includedTypes.indexOf(activity.types.get(0))
                        var tipe = ""
                        if (index != -1)
                        {
                            tipe = activity.nameType[index]
                        }
                        list2.add(
                            AdapterModel(
                                place.id!!,
                                place.name!!,
                                tipe,
                                place.userRatingsTotal?.toString() ?: "0",
                                photoUrl,
                                lat,long,
                                place.rating
                            )
                        )
                    }
                }
                adapters.notifyItemInserted(list2.size - 1)
            }.addOnFailureListener { exception: Exception ->
                Log.e(ContentValues.TAG, "Place not found: ${exception.message}")
            }
    }

    override fun onResume() {
        super.onResume()
        view.post {
            (view.parent as? NonSwipeableViewPager)?.measuredWidthAndState
        }
    }
}