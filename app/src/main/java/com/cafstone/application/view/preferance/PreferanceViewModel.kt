package com.cafstone.application.view.preferance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cafstone.application.data.UserRepository
import com.cafstone.application.data.pref.UserModel
import com.cafstone.application.data.response.ErrorResponse
import com.cafstone.application.view.signup.UserRegisterModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class PreferanceViewModel(private val userRepository: UserRepository) : ViewModel(){

    private val _regist = MutableLiveData<RegistrationStatus>()
    val regist: LiveData<RegistrationStatus>
        get() = _regist

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    sealed class RegistrationStatus {
        data object Loading : RegistrationStatus()
        data class Success(val message: String) : RegistrationStatus()
        data class Error(val message: String) : RegistrationStatus()
    }

    fun register(user:UserRegisterModel) {
        _isLoading.value = true
        viewModelScope.launch {
            _regist.value = RegistrationStatus.Loading
            try {
                val response = userRepository.register(user)
                if (response.success == true) {
                    val userModel = UserModel(user.name,
                        user.email,
                        user.preferences.servesBeer,
                        user.preferences.servesWine,
                        user.preferences.servesCocktails,
                        user.preferences.goodForChildren,
                        user.preferences.goodForGroups,
                        user.preferences.reservable,
                        user.preferences.outdoorSeating,
                        user.preferences.liveMusic,
                        user.preferences.servesDessert,
                        user.preferences.priceLevel,
                        user.preferences.acceptsCreditCards,
                        user.preferences.acceptsDebitCards,
                        user.preferences.acceptsCashOnly,
                        user.preferences.acceptsNfc,
                        true)
                    saveSession(userModel)
                    _regist.value = RegistrationStatus.Success(response.message)

                } else {
                    _regist.value = RegistrationStatus.Error(response.message)
                }
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _regist.value = errorMessage?.let { RegistrationStatus.Error(it) }
            } catch (e: Exception) {
                _regist.value = RegistrationStatus.Error(e.message ?: "Something went wrong during registration")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }
}