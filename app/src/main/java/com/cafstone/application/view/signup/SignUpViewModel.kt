package com.cafstone.application.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cafstone.application.data.UserRepository
import com.cafstone.application.data.response.RegisterResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SignUpViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isStatus = MutableLiveData<RegisterResponse?>()
    val isStatus: LiveData<RegisterResponse?> = _isStatus
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init{
        _isStatus.value = null
        _isLoading.value = false
    }
    fun register(nama :String, email : String, password : String){
        _isLoading.value = true
        runBlocking {
            viewModelScope.launch {
            val a = repository.register(nama,email,password)
            _isStatus.value = a
                _isLoading.value = false
        } }
    }
}