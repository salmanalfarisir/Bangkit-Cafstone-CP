package com.cafstone.application.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cafstone.application.data.UserRepository
import com.cafstone.application.data.pref.UserModel
import com.cafstone.application.data.response.RecomendationResponseItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _recomendation = MutableLiveData<List<RecomendationResponseItem>?>()
    val recomendation: LiveData<List<RecomendationResponseItem>?>
        get() = _recomendation

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getdata() : UserModel{
        return repository.getdata()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

init {
    _isLoading.value = false
    _recomendation.value = null
}
    fun recomendation(user: RecomendationModel) {
        _isLoading.value = true
        viewModelScope.launch {
            val response = repository.recomendation(user)
            _recomendation.value = response
            _isLoading.value = false
        }
    }
}