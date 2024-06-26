package com.cafstone.application.view.preferance

data class PreferenceModel(
    val servesBeer: Boolean,
    val servesWine: Boolean,
    val servesCocktails: Boolean,
    val goodForChildren: Boolean,
    val goodForGroups: Boolean,
    val reservable: Boolean,
    val outdoorSeating: Boolean,
    val liveMusic: Boolean,
    val servesDessert: Boolean,
    val priceLevel: Int,
    val acceptsCreditCards: Boolean,
    val acceptsDebitCards: Boolean,
    val acceptsCashOnly: Boolean,
    val acceptsNfc: Boolean
)