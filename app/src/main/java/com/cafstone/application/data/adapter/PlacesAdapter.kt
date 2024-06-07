package com.cafstone.application.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cafstone.application.R

class PlacesAdapter(private val placesList: List<AdapterModel>) :
    RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val (id, name, desc, photo,_) = placesList[position]
        holder.placeNameTextView.text = name
        holder.placeDescTextView.text = desc
        photo?.let { url ->
            Glide.with(holder.itemView.context)
                .load(url)
                .into(holder.placeImageView)
        }
            ?: holder.placeImageView.setImageResource(R.drawable.no_media_selected) // Set a placeholder image if no photo available
    }

    override fun getItemCount(): Int {
        return placesList.size
    }

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeNameTextView: TextView = itemView.findViewById(R.id.tv_item_name)
        val placeDescTextView: TextView = itemView.findViewById(R.id.story_description)
        val placeImageView: ImageView = itemView.findViewById(R.id.iv_item_photo)
    }
}