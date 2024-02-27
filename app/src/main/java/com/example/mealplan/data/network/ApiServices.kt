package com.example.mealplan.data.network


import ResponseRestaurant
import com.example.mealplan.data.models.detail.ResponseDetailInfo
import com.example.mealplan.data.models.detail.ResponseDetailVideo
import com.example.mealplan.data.models.detail.ResponseNutrition
import com.example.mealplan.data.models.meal.ResponseRandomMeal
import com.example.mealplan.data.models.signup.BodySignUp
import com.example.mealplan.data.models.signup.ResponseSignUp
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ApiServices {
    @POST("users/connect")
    suspend fun postSignUp(@Body body: BodySignUp): Response<ResponseSignUp>

    @GET("food/restaurants/search")
    suspend fun getRestaurants(@Query("min-rating") rate: Int = 5): Response<ResponseRestaurant>

    @GET("recipes/complexSearch")
    suspend fun getMeals(@QueryMap query: Map<String, String>): Response<ResponseRandomMeal>

    @GET("recipes/{id}/information")
    suspend fun getMealInformation(
        @Path("id") id: Int,
        @Query("includeNutrition") info: String = "true"
    ): Response<ResponseDetailInfo>

    @GET("food/videos/search")
    suspend fun getMealVideo(
        @Query("query") title: String
    ): Response<ResponseDetailVideo>

    @GET("recipes/{id}/nutritionWidget.json")
    suspend fun getMealNutrition(
        @Path("id") id: Int
    ): Response<ResponseNutrition>

    @GET("recipes/{id}/nutritionWidget.png")
    suspend fun getMealNutritionChart(
        @Path("id") id: Int
    ): Response<ResponseBody>
}