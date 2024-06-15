package com.cafstone.application.data.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchModel(
    val id: String,
    val title: String,
    val desc : String,
    val att : Double,
    val long : Double,
    val photo: Int
) : Parcelable