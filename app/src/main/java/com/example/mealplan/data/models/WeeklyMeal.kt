package com.example.mealplan.data.models

import com.google.gson.annotations.SerializedName

data class WeeklyMeal(
    @SerializedName("id")
    val id: Int?=null,
    @SerializedName("title")
    val title: String?=null,
    @SerializedName("calories")
    val calories: Int?=null, // 2015
    @SerializedName("carbohydrates")
    val carbohydrates: Int?=null, // 307
    @SerializedName("fat")
    val fat: Int?=null, // 60
    @SerializedName("protein")
    val protein: Int?=null // 57
    // Add other properties specific to the response structure
)
