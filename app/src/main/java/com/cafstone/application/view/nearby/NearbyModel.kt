package com.cafstone.application.view.nearby

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NearbyModel(
    val title : String,
    val desc : String,
    val image : Int,
    val value : String,
):Parcelable
