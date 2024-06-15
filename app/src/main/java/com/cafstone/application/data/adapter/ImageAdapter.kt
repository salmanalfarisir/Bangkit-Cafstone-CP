package com.cafstone.application.data.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.cafstone.application.R

class ImageAdapter(private val photoUris: List<Uri>) : RecyclerView.Adapter<ImageAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_item_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_review, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val uri = photoUris[position]
        val req : RequestOptions = RequestOptions().override(Target.SIZE_ORIGINAL)
        Glide.with(holder.itemView.context)
            .load(uri)
            .apply(req)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = photoUris.size
}
