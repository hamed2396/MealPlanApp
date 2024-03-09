package com.example.mealplan.data.models.detail


import com.google.gson.annotations.SerializedName

class ResponseSimilar : ArrayList<ResponseSimilar.ResponseSimilarItem>(){
    data class ResponseSimilarItem(
        @SerializedName("id")
        val id: Int?, // 31868
        @SerializedName("imageType")
        val imageType: String?, // jpg
        @SerializedName("readyInMinutes")
        val readyInMinutes: Int?, // 45
        @SerializedName("servings")
        val servings: Int?, // 2
        @SerializedName("sourceUrl")
        val sourceUrl: String?, // http://www.seriouseats.com/recipes/2009/06/dinner-tonight-chickpea-bruschetta-babbo-nyc-recipe.html
        @SerializedName("title")
        val title: String? // Dinner Tonight: Chickpea Bruschetta
    )
}