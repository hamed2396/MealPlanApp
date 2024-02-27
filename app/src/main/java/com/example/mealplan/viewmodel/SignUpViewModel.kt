package com.example.mealplan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.kotlinextensions.force
import com.example.mealplan.data.models.signup.BodySignUp
import com.example.mealplan.data.models.signup.ResponseSignUp
import com.example.mealplan.data.repository.SignUpRepository
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.network.NetworkResponse
import com.example.mealplan.utils.network.NetworkStatus
import com.example.mealplan.utils.network.NetworkStatus.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: SignUpRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {

    private val _signUp = MutableLiveData<NetworkStatus<ResponseSignUp>>()
    val signUp get() = _signUp.force<LiveData<NetworkStatus<ResponseSignUp>>>()

fun postSignUp(body: BodySignUp) = viewModelScope.launch {
        _signUp.postValue(Loading())
        try {
            val response = repository.postSignUp(body)
            _signUp.postValue(networkResponse.handleResponse(response))
        } catch (_: Exception) {
            _signUp.postValue(Error(Constants.CHECK_CONNECTION))
        }

    }
}