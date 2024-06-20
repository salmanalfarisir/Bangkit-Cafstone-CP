package com.cafstone.application.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.cafstone.application.R
import com.cafstone.application.databinding.UlasanItemViewBinding

class UlasanAdapter(private val placesList: List<UlasanModel>) :
    RecyclerView.Adapter<UlasanAdapter.PlaceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding =
            UlasanItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val (photo, nama, rating, publish, teks) = placesList[position]
        val req: RequestOptions = RequestOptions().override(Target.SIZE_ORIGINAL)
        photo?.let { url ->
            Glide.with(holder.itemView.context)
                .load(url)
                .apply(req)
                .into(holder.binding.profileImage)
        }
            ?: Glide.with(holder.itemView.context)
                .load("https://cdn.idntimes.com/content-images/post/20240207/33bac083ba44f180c1435fc41975bf36-ca73ec342155d955387493c4eb78c8bb.jpg")
                .apply(req)
                .into(holder.binding.profileImage)

        if (rating >= 1) {
            for (i in 1..rating.toInt()) {
                when (i) {
                    1 -> {
                        holder.binding.star1.setImageResource(R.drawable.emojione_star)
                    }

                    2 -> {
                        holder.binding.star2.setImageResource(R.drawable.emojione_star)
                    }

                    3 -> {
                        holder.binding.star3.setImageResource(R.drawable.emojione_star)
                    }

                    4 -> {
                        holder.binding.star4.setImageResource(R.drawable.emojione_star)
                    }

                    5 -> {
                        holder.binding.star5.setImageResource(R.drawable.emojione_star)
                    }
                }
            }
        }

        if (publish != null) {
            holder.binding.postTime.text = publish
        } else {
            holder.binding.postTime.visibility = View.GONE
        }

        holder.binding.nama.text = nama

        if (teks != null) {
            holder.binding.textReview.text = teks
        } else {
            holder.binding.textReview.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return placesList.size
    }

    class PlaceViewHolder(val binding: UlasanItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}