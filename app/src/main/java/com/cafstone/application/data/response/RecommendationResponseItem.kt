package com.cafstone.application.data.response

import com.google.gson.annotations.SerializedName

data class RecommendationResponseItem(

    @field:SerializedName("acceptsDebitCards")
    val acceptsDebitCards: Boolean,

    @field:SerializedName("servesBeer")
    val servesBeer: Boolean,

    @field:SerializedName("displayName")
    val displayName: String,

    @field:SerializedName("reservable")
    val reservable: Boolean,

    @field:SerializedName("rating")
    val rating: String,

    @field:SerializedName("acceptsNfc")
    val acceptsNfc: Boolean,

    @field:SerializedName("servesCocktails")
    val servesCocktails: Boolean,

    @field:SerializedName("Type_encoded")
    val typeEncoded: Int,

    @field:SerializedName("priceLevel")
    val priceLevel: Int,

    @field:SerializedName("acceptsCashOnly")
    val acceptsCashOnly: Boolean,

    @field:SerializedName("goodForChildren")
    val goodForChildren: Boolean,

    @field:SerializedName("Type")
    val type: String,

    @field:SerializedName("formattedAddress")
    val formattedAddress: String,

    @field:SerializedName("servesDessert")
    val servesDessert: Boolean,

    @field:SerializedName("paymentOptions")
    val paymentOptions: String,

    @field:SerializedName("outdoorSeating")
    val outdoorSeating: Boolean,

    @field:SerializedName("acceptsCreditCards")
    val acceptsCreditCards: Boolean,

    @field:SerializedName("servesWine")
    val servesWine: Boolean,

    @field:SerializedName("goodForGroups")
    val goodForGroups: Boolean,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("liveMusic")
    val liveMusic: Boolean
)
