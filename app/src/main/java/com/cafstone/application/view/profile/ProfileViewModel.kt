package com.cafstone.application.view.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cafstone.application.data.UserRepository
import com.cafstone.application.data.pref.UserModel
import com.cafstone.application.data.response.ErrorResponse
import com.cafstone.application.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    private val _password = MutableLiveData<RegisterResponse>()
    val password: LiveData<RegisterResponse>
        get() = _password

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getData(): UserModel {
        return repository.getdata()
    }

    fun password(oldPassword: String, newPassword: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val data = getData()
                val response = repository.changepassword(data.email, oldPassword, newPassword)
                _password.value = response
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                _password.value = errorMessage?.let { RegisterResponse(false, it) }
            } catch (e: Exception) {
                _password.value = RegisterResponse(false, e.message.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }
}