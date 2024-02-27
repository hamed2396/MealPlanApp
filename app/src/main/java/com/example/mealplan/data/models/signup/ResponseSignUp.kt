package com.example.mealplan.data.models.signup


import com.google.gson.annotations.SerializedName

data class ResponseSignUp(
    @SerializedName("hash")
    val hash: String?, // q572587bq2405724q05
    @SerializedName("spoonacularPassword")
    val spoonacularPassword: String?, // meadwith31grapejam
    @SerializedName("username")
    val username: String? // api_123_user
)