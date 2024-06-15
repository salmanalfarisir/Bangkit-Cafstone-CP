package com.cafstone.application.data.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cafstone.application.R
import com.cafstone.application.view.Nearby.NearbyActivity
import com.cafstone.application.view.Nearby.NearbyModel

class SearchAdapter(private val placesList: List<SearchModel>) :
    RecyclerView.Adapter<SearchAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item_view, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val (id, title, desc,att,long, photo) = placesList[position]
        holder.placeImageView.setImageResource(photo)
        holder.placeNameTextView.text = title
        holder.itemView.setOnClickListener{
            val data = NearbyModel(
                title,
                "Ingin Mencoba ${desc}? Berikut Rekomendasinya.",
                photo,
                id
            )
            val intent = Intent(holder.itemView.context, NearbyActivity::class.java)
            intent.putExtra(NearbyActivity.EXTRA_ATT, data)
            intent.putExtra(NearbyActivity.LATITUDE, att)
            intent.putExtra(NearbyActivity.LONGTITUDE, long)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return placesList.size
    }

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeNameTextView: TextView = itemView.findViewById(R.id.textView)
        val placeImageView: ImageView = itemView.findViewById(R.id.image_view)
    }
}