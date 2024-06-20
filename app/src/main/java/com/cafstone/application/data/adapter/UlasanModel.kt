package com.cafstone.application.data.adapter

import android.net.Uri
import android.os.Parcelable
import com.google.android.libraries.places.api.model.PhotoMetadata
import kotlinx.parcelize.Parcelize

@Parcelize
data class UlasanModel(
    val profile : String?,
    val name : String,
    val rating : Double,
    val publish : String?,
    val teks : String?
) : Parcelable