package com.example.mealplan.data.models.search

data class SearchFilterModel(
    var minCalRange: String? = null,
    var maxCalRange: String? = null,
    var minCarbRange: String? = null,
    var maxCarbRange: String? = null,
    var minProteinRange: String? = null,
    var maxProteinRange: String? = null,
    var minFaRange: String? = null,
    var maxFatRange: String? = null,
    var intolerance: String? = null,
    var diet: String? = null,
    var cuisine: String? = null,
)