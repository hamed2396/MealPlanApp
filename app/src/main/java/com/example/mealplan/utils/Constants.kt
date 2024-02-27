package com.example.mealplan.utils

import androidx.lifecycle.MutableLiveData

object Constants {
    //network
    const val BASE_URL = "https://api.spoonacular.com/"
    const val CONNECTION_TIME_OUT = 60L
    const val PING_TIME_OUT = 3L
    const val PING_NAMED = "ping_named"
    const val AUTH = "x-api-key"
    const val API_KEY = "23ff98ff47fe4031902b529a052ff71f"
    const val ERROR_CONNECTION = "Something went wrong"
    const val CHECK_CONNECTION = "Check Your Connection"
    const val AUTHORIZATION_ERROR = "You Are Not Authorized"
    const val SERVER_ERROR = "Something Went Down in our End"
    const val BASE_URL_IMAGES = "https://spoonacular.com/cdn/ingredients_"
    const val SIZE_IMAGES = "100x100/"


    //other
    const val DOUBLE_BACK_PRESS_DELAY = 2750L // its the length of the long snackBar
    const val MAX_BACK_PRESS_COUNT = 2
    const val FAT = "Fat"
    const val CARBS = "Carbs"
    const val PROTEIN = "Protein"
    const val BREAD = "Bread"
    const val BREAKFAST = "Breakfast"
    const val SOUP = "Soup"
    const val DRINK = "Drink"
    const val SNACK = "Snack"
    const val TYPE = "type"
    const val NUMBER = "number"
    const val QUERY = "query"
    const val ADD_RECIPE_NUTRITION = "addRecipeNutrition"
    var hasAppliedFilters = false
    var hasClearedFilters = false
    var MIN_CARBS = "minCarbs"
    var Max_CARBS = "maxCarbs"
    var MIN_PROTEIN = "minProtein"
    var Max_PROTEIN = "maxProtein"
    var MIN_FAT = "minFat"
    var Max_FAT = "maxFat"
    var MIN_CALORIES = "minCalories"
    var Max_CALORIES = "maxCalories"
    var INTOLERANCES = "intolerances"
    var CUISINE = "cuisine"
    var DIET = "diet"
    var isFiltersEnabled = MutableLiveData<Boolean>()
    var isSwitchEnabled = false
    const val VITAMINS = "Vitamins"
    const val NUTRITION = "Nutrition"
    var FIBER_INDEX: Int? = 14
    var NET_CARBOHYDRATES_INDEX: Int? = 4
    var SUGAR_INDEX: Int? = null
    var SATURATED_FAT_INDEX: Int? = 2
    var VitaminC_INDEX: Int? = 0
    var VitaminB3_INDEX: Int? = 0
    var VitaminE_INDEX: Int? = 0
    var VitaminB1_INDEX: Int? = 0
    var STEPS_COUNT = 0
    var STEPS_COUNT_STEP_FRAGMENT: Int? = null


}