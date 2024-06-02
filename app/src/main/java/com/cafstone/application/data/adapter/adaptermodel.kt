package com.cafstone.application.data.adapter

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class adaptermodel(val id: String,
                     val name: String,
                     val desc: String,
                     val photo: String?) : Parcelable