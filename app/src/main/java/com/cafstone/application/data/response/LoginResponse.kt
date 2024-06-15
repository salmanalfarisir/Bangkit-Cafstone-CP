package com.cafstone.application.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class Data(

	@field:SerializedName("uid")
	val uid: String,

	@field:SerializedName("preferences")
	val preferences: Preferences,

	@field:SerializedName("name")
	val name: String
)

data class Preferences(

	@field:SerializedName("acceptsDebitCards")
	val acceptsDebitCards: Boolean,

	@field:SerializedName("servesBeer")
	val servesBeer: Boolean,

	@field:SerializedName("reservable")
	val reservable: Boolean,

	@field:SerializedName("servesCocktails")
	val servesCocktails: Boolean,

	@field:SerializedName("acceptsNfc")
	val acceptsNfc: Boolean,

	@field:SerializedName("priceLevel")
	val priceLevel: Int,

	@field:SerializedName("goodForChildren")
	val goodForChildren: Boolean,

	@field:SerializedName("acceptsCashOnly")
	val acceptsCashOnly: Boolean,

	@field:SerializedName("servesDessert")
	val servesDessert: Boolean,

	@field:SerializedName("outdoorSeating")
	val outdoorSeating: Boolean,

	@field:SerializedName("servesWine")
	val servesWine: Boolean,

	@field:SerializedName("acceptsCreditCards")
	val acceptsCreditCards: Boolean,

	@field:SerializedName("goodForGroups")
	val goodForGroups: Boolean,

	@field:SerializedName("liveMusic")
	val liveMusic: Boolean
)
