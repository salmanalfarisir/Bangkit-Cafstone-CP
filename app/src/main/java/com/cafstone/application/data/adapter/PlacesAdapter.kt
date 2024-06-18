package com.cafstone.application.data.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.cafstone.application.R
import com.cafstone.application.di.PlacesClientSingleton
import com.cafstone.application.view.detail.DetailActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.net.FetchResolvedPhotoUriRequest

class PlacesAdapter(private val placesList: List<AdapterModel>) :
    RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val (id, name, desc, photo,lat, long, rating) = placesList[position]
        holder.placeNameTextView.text = name
        holder.placeDescTextView.text = desc
        photo?.let { url ->
            val placesClient = PlacesClientSingleton.getInstance(holder.itemView.context)
            val photoRequest = FetchResolvedPhotoUriRequest.builder(url)
                .setMaxWidth(500)
                .setMaxHeight(280)
                .build()

            placesClient.fetchResolvedPhotoUri(photoRequest)
                .addOnSuccessListener { fetchResolvedPhotoUriResponse ->
                    val uri = fetchResolvedPhotoUriResponse.uri
                    val req: RequestOptions = RequestOptions().override(Target.SIZE_ORIGINAL)
                    Glide.with(holder.itemView.context)
                        .load(uri)
                        .apply(req)
                        .into(holder.placeImageView)
                }.addOnFailureListener { exception ->
                if (exception is ApiException) {
                    Log.e("MainActivity", "Place not found: ${exception.message}")
                    holder.placeImageView.setImageResource(R.drawable.no_media_selected)
                }
            }
        }
            ?: holder.placeImageView.setImageResource(R.drawable.no_media_selected) // Set a placeholder image if no photo available
        if (rating == null) {
            holder.ratingTextView.visibility = View.GONE
            holder.ratingImageView.visibility = View.GONE
        } else {
            holder.ratingTextView.text = rating.toString()
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

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeNameTextView: TextView = itemView.findViewById(R.id.placeName)
        val placeDescTextView: TextView = itemView.findViewById(R.id.story_description)
        val placeImageView: ImageView = itemView.findViewById(R.id.iv_item_photo)
        val ratingImageView: ImageView = itemView.findViewById(R.id.imageView2)
        val ratingTextView: TextView = itemView.findViewById(R.id.ratingtextview)
    }
}