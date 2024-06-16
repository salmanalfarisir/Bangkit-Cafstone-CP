package com.cafstone.application.data.retrofit

import com.cafstone.application.data.response.LoginResponse
import com.cafstone.application.data.response.RegisterResponse
import com.cafstone.application.view.signup.UserRegisterModel
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    suspend fun register(@Body requestBody: UserRegisterModel): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @FormUrlEncoded
    @POST("change-password")
    suspend fun changepassword(
        @Field("email") email: String,
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String
    ): RegisterResponse
}