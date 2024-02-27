package com.example.mealplan.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.kotlinextensions.force
import com.example.mealplan.data.models.meal.ResponseRandomMeal
import com.example.mealplan.data.repository.SearchRepository
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.network.NetworkResponse
import com.example.mealplan.utils.network.NetworkStatus
import com.example.mealplan.utils.network.NetworkStatus.Error
import com.example.mealplan.utils.network.NetworkStatus.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {

    fun mealsList(): MutableList<String> {
        return mutableListOf(
            "Main Course",
            "Bread",
            "Marinade",
            "Side Dish",
            "Breakfast",
            "Dessert",
            "Soup",
            "Snack",
            "Appetizer",
            "Beverage",
            "Drink",
            "Salad",
            "Sauce"
        )
    }

    fun intolerancesList(): MutableList<String> {
        return mutableListOf(
            "Dairy",
            "Peanut",
            "Soy",
            "Egg",
            "Seafood",
            "Sulfite",
            "Sesame",
            "Tree Nut",
            "Grain",
            "Shellfish",
            "Wheat"
        )
    }

    fun dietList(): MutableList<String> {
        return mutableListOf(
            "Gluten Free",
            "Ketogenic",
            "Vegetarian",
            "Lacto-Vegetarian",
            "Ovo-Vegetarian",
            "Pescetarian",
            "Paleo",
            "Low FODMAP",
            "Primal",
        )
    }

    fun cuisinesList(): MutableList<String> {
        return mutableListOf(
            "Chinese",
            "Asian",
            "Mexican",
            "Italian",
            "Indian",
            "Japanese",
            "Greek",
            "Thai",
            "Korean",
        )
    }


    fun provideQueries(
        type: String? = null,
        title: String? = null,
        minCalRange: String? = null,
        maxCalRange: String? = null,
        minCarbRange: String? = null,
        maxCarbRange: String? = null,
        minProteinRange: String? = null,
        maxProteinRange: String? = null,
        minFatRange: String? = null,
        maxFatRange: String? = null,
        intolerance: String? = null,
        diet: String? = null,
        cuisine: String? = null

    ): HashMap<String, String> {
        val queries = hashMapOf<String, String>()
        queries[Constants.ADD_RECIPE_NUTRITION] = "true"
        type?.let { queries[Constants.TYPE] = it }
        title?.let { queries[Constants.QUERY] = it }
        minCalRange?.let { queries[Constants.MIN_CALORIES] = it }
        maxCalRange?.let { queries[Constants.Max_CALORIES] = it }
        minCarbRange?.let { queries[Constants.MIN_CARBS] = it }
        maxCarbRange?.let { queries[Constants.Max_CARBS] = it }
        minProteinRange?.let { queries[Constants.MIN_PROTEIN] = it }
        maxProteinRange?.let { queries[Constants.Max_PROTEIN] = it }
        minFatRange?.let { queries[Constants.MIN_FAT] = it }
        maxFatRange?.let { queries[Constants.Max_FAT] = it }
        intolerance?.let { queries[Constants.INTOLERANCES] = it }
        diet?.let { queries[Constants.DIET] = it }
        cuisine?.let { queries[Constants.CUISINE] = it }
        return queries
    }

    private val _searchMeals = MutableLiveData<NetworkStatus<ResponseRandomMeal>>()
    val searchMeals get() = _searchMeals.force<LiveData<NetworkStatus<ResponseRandomMeal>>>()

    fun callSearchMeals(queries: Map<String, String>) = viewModelScope.launch {
        _searchMeals.postValue(Loading())
        try {
            val response = repository.getSearchMeals(queries)
            _searchMeals.postValue(networkResponse.handleResponse(response))
        } catch (_: Exception) {
            _searchMeals.postValue(Error(Constants.CHECK_CONNECTION))
        }

    }
}