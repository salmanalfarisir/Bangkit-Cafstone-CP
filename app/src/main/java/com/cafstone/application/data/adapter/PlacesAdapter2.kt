package com.cafstone.application.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cafstone.application.R

class PlacesAdapter2(private val placesList: List<AdapterModel>): RecyclerView.Adapter<PlacesAdapter2.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story2, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val (id, name, desc, photo,rating) = placesList[position]
        holder.placeNameTextView.text = name
        holder.placeDescTextView.text = desc
        photo?.let { url ->
            Glide.with(holder.itemView.context)
                .load(url)
                .into(holder.placeImageView)
        }
            ?: holder.placeImageView.setImageResource(R.drawable.no_media_selected)
        if (rating== null)
        {
            holder.ratingTextView.visibility = View.GONE
            holder.ratingImageView.visibility = View.GONE
        }else{
            holder.ratingTextView.text = rating.toString()
        }
    }

    override fun getItemCount(): Int {
        return placesList.size
    }

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeNameTextView: TextView = itemView.findViewById(R.id.tv_item_name)
        val placeDescTextView: TextView = itemView.findViewById(R.id.story_description)
        val placeImageView: ImageView = itemView.findViewById(R.id.iv_item_photo)
        val ratingImageView: ImageView = itemView.findViewById(R.id.imageView2)
        val ratingTextView: TextView = itemView.findViewById(R.id.ratingtextview)
    }
}