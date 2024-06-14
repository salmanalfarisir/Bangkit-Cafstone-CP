package com.cafstone.application.data.retrofit

import com.cafstone.application.data.response.RecomendationResponseItem
import com.cafstone.application.view.main.RecomendationModel
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService2 {
    @POST("recommend_by_preferences")
    suspend fun getrecomendation(@Body requestBody: RecomendationModel): List<RecomendationResponseItem>
}