package com.cafstone.application.data.adapter

import android.content.Intent
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.cafstone.application.R
import com.cafstone.application.databinding.ItemRvRekomendasiLanjutanBinding
import com.cafstone.application.di.PlacesClientSingleton
import com.cafstone.application.view.detail.DetailActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest

class PlacesAdapter2(private val placesList: List<AdapterModel>) :
    RecyclerView.Adapter<PlacesAdapter2.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemRvRekomendasiLanjutanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val (id, name, desc, total, photo, lat, long, destlat, destlong, rating) = placesList[position]
        holder.bind(name, desc, total)
        photo?.let { url ->
            val placesClient = PlacesClientSingleton.getInstance(holder.itemView.context)
            val photoRequest = FetchResolvedPhotoUriRequest.builder(url)
                .setMaxWidth(200)
                .setMaxHeight(280)
                .build()

            placesClient.fetchResolvedPhotoUri(photoRequest)
                .addOnSuccessListener { fetchResolvedPhotoUriResponse ->
                    val uri = fetchResolvedPhotoUriResponse.uri
                    val req: RequestOptions = RequestOptions().override(Target.SIZE_ORIGINAL)
                    Glide.with(holder.itemView.context)
                        .load(uri)
                        .apply(req)
                        .into(holder.binding.rekomendasiPhoto)
                }.addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        Log.e("MainActivity", "Place not found: ${exception.message}")
                        holder.binding.rekomendasiPhoto.setImageResource(R.drawable.no_image)
                    }
                }
        }
            ?: holder.binding.rekomendasiPhoto.setImageResource(R.drawable.no_image)
        if (rating == null) {
            holder.binding.rekomendasiPhotoRating.visibility = View.GONE
            holder.binding.ratingTextView1.visibility = View.GONE
        } else {
            holder.binding.ratingTextView1.text = rating.toString()
        }

        if (destlat != 0.0 && destlong != 0.0) {
            val currentLocation = Location("currentLocation").apply {
                latitude = lat
                longitude = long
            }

            // Membuat objek Location untuk tujuan
            val destinationLocation = Location("destinationLocation").apply {
                latitude = destlat
                longitude = destlong
            }

            val distanceInMeters = currentLocation.distanceTo(destinationLocation)
            val distanceText = if (distanceInMeters >= 1000) {
                val distanceInKilometers = distanceInMeters / 1000
                "%.2f KM".format(distanceInKilometers)
            } else {
                "${distanceInMeters.toInt()} M"
            }
            holder.binding.jarak.text = distanceText
        } else {
            holder.binding.jarak.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.PLACE_ID, id)
            intentDetail.putExtra(DetailActivity.lat, lat)
            intentDetail.putExtra(DetailActivity.long, long)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int {
        return placesList.size
    }

    class PlaceViewHolder(val binding: ItemRvRekomendasiLanjutanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String, desc: String, total: String) {
            binding.rekomendasiPlaceName.text = name
            binding.rekomendasiPlaceType.text = desc
            binding.reviewTextView2.text = "(${total})"
        }
    }
}