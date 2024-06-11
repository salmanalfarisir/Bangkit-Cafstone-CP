package com.cafstone.application.data.retrofit

import com.cafstone.application.data.response.LoginResponse
import com.cafstone.application.data.response.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("servesBeer") servesBeer: Boolean,
        @Field("servesWine") servesWine: Boolean,
        @Field("servesCocktails") servesCocktails: Boolean,
        @Field("goodForChildren") goodForChildren: Boolean,
        @Field("goodForGroups") goodForGroups: Boolean,
        @Field("reservable") reservable: Boolean,
        @Field("outdoorSeating") outdoorSeating: Boolean,
        @Field("liveMusic") liveMusic: Boolean,
        @Field("servesDessert") servesDessert: Boolean,
        @Field("priceLevel") priceLevel: Int,
        @Field("acceptsCreditCards") acceptsCreditCards: Boolean,
        @Field("acceptsDebitCards") acceptsDebitCards: Boolean,
        @Field("acceptsCashOnly") acceptsCashOnly: Boolean,
        @Field("acceptsNfc") acceptsNfc: Boolean,
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse
}