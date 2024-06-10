package com.cafstone.application.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cafstone.application.data.UserRepository
import com.cafstone.application.data.pref.UserModel
import com.cafstone.application.data.response.LoginResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _login = MutableLiveData<LoginResponse>()
    val login: LiveData<LoginResponse> = _login

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _error.value = ""
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                _login.value = response
                if (response.success) {
                } else {
                    val errorMessage = response.message ?: "Login failed"
                    _error.value = errorMessage
                }
            } catch (e: Exception) {
                val networkErrorMessage = "Network error occurred"
                _error.value = e.message ?: networkErrorMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun seterror() {
        _error.value = ""
    }
}
