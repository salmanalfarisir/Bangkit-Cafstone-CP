package com.cafstone.application.view.signup

import com.cafstone.application.view.preferance.PreferenceModel

data class UserRegisterModel(
    val name : String,
    val email: String,
    val password : String,
    val preferences : PreferenceModel
)