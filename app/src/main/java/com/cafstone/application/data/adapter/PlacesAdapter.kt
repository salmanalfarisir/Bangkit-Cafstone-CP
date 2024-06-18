package com.cafstone.application.data.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.cafstone.application.R
import com.cafstone.application.databinding.ItemStoryBinding
import com.cafstone.application.di.PlacesClientSingleton
import com.cafstone.application.view.detail.DetailActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest

class PlacesAdapter(private val placesList: List<AdapterModel>) :
    RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val (id, name, desc,total, photo,lat,long, rating) = placesList[position]
        holder.bind(name,desc,total)
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
                        holder.binding.rekomendasiPhoto.setImageResource(R.drawable.no_media_selected)
                    }
                }
        }
            ?: holder.binding.rekomendasiPhoto.setImageResource(R.drawable.no_media_selected)
        if (rating == null) {
            holder.binding.rekomendasiPhotoRating.visibility = View.GONE
            holder.binding.ratingtextview.visibility = View.GONE
        } else {
            holder.binding.ratingtextview.text = rating.toString()
        }

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.PLACE_ID, id)
            intentDetail.putExtra(DetailActivity.lat,lat)
            intentDetail.putExtra(DetailActivity.long,long)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int {
        return placesList.size
    }

    class PlaceViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(name : String,desc : String,total : String){
            binding.rekomendasiplaceName.text = name
            binding.rekomendasiplaceType.text = desc
            binding.reviewtextview.text = "(${total})"
        }
    }
}