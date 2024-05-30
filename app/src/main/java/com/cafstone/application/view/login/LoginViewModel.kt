package com.cafstone.application.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cafstone.application.data.UserRepository
import com.cafstone.application.data.response.LoginResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isStatus = MutableLiveData<LoginResponse?>()
    val isStatus: LiveData<LoginResponse?> = _isStatus
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = false
        _isStatus.value = null
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        runBlocking {
            viewModelScope.launch {
                val a = repository.login(email, password)
                _isStatus.value = a
                _isLoading.value = false
            }
        }
    }
}