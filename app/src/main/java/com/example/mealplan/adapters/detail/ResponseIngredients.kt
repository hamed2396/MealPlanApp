package com.example.mealplan.adapters.detail


import com.google.gson.annotations.SerializedName

data class ResponseIngredients(
    @SerializedName("ingredients")
    val ingredients: List<Ingredient>?
) {
    data class Ingredient(
        @SerializedName("amount")
        val amount: Amount?,
        @SerializedName("image")
        val image: String?, // dry-cannellini-beans.jpg
        @SerializedName("name")
        val name: String? // cooked white dried cannellini beans
    ) {
        data class Amount(
            @SerializedName("metric")
            val metric: Metric?,
            @SerializedName("us")
            val us: Us?
        ) {
            data class Metric(
                @SerializedName("unit")
                val unit: String?, // g
                @SerializedName("value")
                val value: Double? // 757.5
            )

            data class Us(
                @SerializedName("unit")
                val unit: String?, // cups
                @SerializedName("value")
                val value: Double? // 3.75
            )
        }
    }
}