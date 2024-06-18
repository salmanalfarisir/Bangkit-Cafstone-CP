package com.cafstone.application.data.adapter

import android.os.Parcelable
import com.google.android.libraries.places.api.model.PhotoMetadata
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdapterModel(
    val id: String,
    val name: String,
    val desc: String,
    val photo: PhotoMetadata?,
    val lat : Double,
    val lang : Double,
    val rating: Double?
) : Parcelable