package com.cafstone.application.data.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdapterModel(
    val id: String,
    val name: String,
    val desc: String,
    val photo: String?,
    val rating:Double?
) : Parcelable