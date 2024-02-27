package com.example.mealplan.viewmodel

import ResponseRestaurant
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.kotlinextensions.force
import com.example.mealplan.data.models.meal.ResponseRandomMeal
import com.example.mealplan.data.repository.MealRepository
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
class MealViewModel @Inject constructor(
    private val repository: MealRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {






    init {
        viewModelScope.launch {
            delay(300)
           getRandomMeals(provideQueries())
        }

    }
    fun provideQueries(type: String? = null,numberResult:Boolean?=null): HashMap<String, String> {
        val queries = hashMapOf<String, String>()
        type?.let { queries[Constants.TYPE] = it }
        numberResult?.let { queries[Constants.NUMBER] = "10" }
        queries[Constants.ADD_RECIPE_NUTRITION]="true"
        return queries
    }

    private val _randomMeals = MutableLiveData<NetworkStatus<ResponseRandomMeal>>()
    val randomMeals get() = _randomMeals.force<LiveData<NetworkStatus<ResponseRandomMeal>>>()

     fun getRandomMeals(queries:Map<String,String>) = viewModelScope.launch {
        _randomMeals.postValue(Loading())
        try {
            val response = repository.getRandomMeals(queries)
            _randomMeals.postValue(networkResponse.handleResponse(response))
        } catch (_: Exception) {
            _randomMeals.postValue(Error(Constants.CHECK_CONNECTION))
        }

    }
}