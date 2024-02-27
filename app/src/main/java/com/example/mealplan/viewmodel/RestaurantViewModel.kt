package com.example.mealplan.viewmodel

import ResponseRestaurant
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.kotlinextensions.force
import com.example.mealplan.data.models.meal.ResponseRandomMeal
import com.example.mealplan.data.repository.MealRepository
import com.example.mealplan.data.repository.RestaurantRepository
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.network.NetworkResponse
import com.example.mealplan.utils.network.NetworkStatus
import com.example.mealplan.utils.network.NetworkStatus.Error
import com.example.mealplan.utils.network.NetworkStatus.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val repository: RestaurantRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {


    private val _restaurant = MutableLiveData<NetworkStatus<ResponseRestaurant>>()
    val restaurantData get() = _restaurant.force<LiveData<NetworkStatus<ResponseRestaurant>>>()

     fun getRestaurant() = viewModelScope.launch {
        _restaurant.postValue(Loading())
        try {
            val response = repository.getRestaurants()
            _restaurant.postValue(networkResponse.handleResponse(response))
        } catch (_: Exception) {
            _restaurant.postValue(Error(Constants.CHECK_CONNECTION))
        }

    }

    }
