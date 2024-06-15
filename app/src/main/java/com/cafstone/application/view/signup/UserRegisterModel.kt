package com.cafstone.application.view.signup

import com.cafstone.application.view.preferance.PreferencesData


data class UserRegisterModel(
    val name : String,
    val email: String,
    val password : String,
    val preferences : PreferencesData
)