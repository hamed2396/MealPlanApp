package com.example.mealplan.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterSearchViewModel @Inject constructor(

) : ViewModel() {

    var minCalRange: String? = null
    var maxCalRange: String? = null
    var minCarbRange: String? = null
    var maxCarbRange: String? = null
    var minProteinRange: String? = null
    var maxProteinRange: String? = null
    var minFatRange: String? = null
    var maxFatRange: String? = null
    var intolerance: String? = null
    var diet: String? = null
    var cuisine: String? = null


}