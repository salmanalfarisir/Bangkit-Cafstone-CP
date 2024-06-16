package com.cafstone.application.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cafstone.application.data.UserRepository
import com.cafstone.application.data.pref.UserModel
import com.cafstone.application.data.response.RecommendationResponseItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _recommendation = MutableLiveData<List<RecommendationResponseItem>?>()
    val recommendation: LiveData<List<RecommendationResponseItem>?>
        get() = _recommendation

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getData(): UserModel {
        return repository.getdata()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    init {
        _isLoading.value = false
        _recommendation.value = null
    }

    fun recommendation(user: RecommendationModel) {
        _isLoading.value = true
        viewModelScope.launch {
            val response = repository.recomendation(user)
            _recommendation.value = response
            _isLoading.value = false
        }
    }
}