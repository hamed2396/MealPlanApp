package com.example.mealplan.data.models.signup


import com.google.gson.annotations.SerializedName

data class BodySignUp(
    @SerializedName("email")
    var email: String?=null, // your user's email
    @SerializedName("firstName")
    var firstName: String?=null, // your user's first name
    @SerializedName("lastName")
    var lastName: String?=null, // your user's last name
    @SerializedName("username")
    var username: String?=null // your user's name
)