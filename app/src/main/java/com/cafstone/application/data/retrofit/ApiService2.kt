package com.cafstone.application.data.retrofit

import com.cafstone.application.data.response.RecommendationResponseItem
import com.cafstone.application.view.main.RecommendationModel
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService2 {
    @POST("recommend_by_preferences")
    suspend fun getRecommendation(@Body requestBody: RecommendationModel): List<RecommendationResponseItem>
}