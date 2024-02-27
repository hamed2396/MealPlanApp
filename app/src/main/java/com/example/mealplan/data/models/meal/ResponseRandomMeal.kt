package com.example.mealplan.data.models.meal


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
@Parcelize
data class ResponseRandomMeal(
    @SerializedName("number")
    val number: Int?, // 10
    @SerializedName("offset")
    val offset: Int?, // 0
    @SerializedName("results")
    val results: @RawValue List<Result>?,
    @SerializedName("totalResults")
    val totalResults: Int? // 5218
) : Parcelable {
    @Parcelize data class Result(
        @SerializedName("aggregateLikes")
        val aggregateLikes: Int?, // 309
        @SerializedName("analyzedInstructions")
        val analyzedInstructions: List<AnalyzedInstruction?>?,
        @SerializedName("cheap")
        val cheap: Boolean?, // false
        @SerializedName("cookingMinutes")
        val cookingMinutes: Int?, // -1
        @SerializedName("creditsText")
        val creditsText: String?, // blogspot.com
        @SerializedName("cuisines")
        val cuisines: List<String?>?,
        @SerializedName("dairyFree")
        val dairyFree: Boolean?, // true
        @SerializedName("diets")
        val diets: List<String?>?,
        @SerializedName("dishTypes")
        val dishTypes: List<String?>?,
        @SerializedName("gaps")
        val gaps: String?, // no
        @SerializedName("glutenFree")
        val glutenFree: Boolean?, // true
        @SerializedName("healthScore")
        val healthScore: Int?, // 100
        @SerializedName("id")
        val id: Int?, // 782585
        @SerializedName("image")
        val image: String?, // https://spoonacular.com/recipeImages/782585-312x231.jpg
        @SerializedName("imageType")
        val imageType: String?, // jpg
        @SerializedName("license")
        val license: String?, // CC BY-SA 3.0
        @SerializedName("lowFodmap")
        val lowFodmap: Boolean?, // false
        @SerializedName("nutrition")
        val nutrition: @RawValue Nutrition?,
        @SerializedName("occasions")
        val occasions: List<String?>?,
        @SerializedName("preparationMinutes")
        val preparationMinutes: Int?, // -1
        @SerializedName("pricePerServing")
        val pricePerServing: Double?, // 134.63
        @SerializedName("readyInMinutes")
        val readyInMinutes: Int?, // 45
        @SerializedName("servings")
        val servings: Int?, // 6
        @SerializedName("sourceName")
        val sourceName: String?, // blogspot.com
        @SerializedName("sourceUrl")
        val sourceUrl: String?, // http://foodandspice.blogspot.com/2016/05/cannellini-bean-and-asparagus-salad.html
        @SerializedName("spoonacularScore")
        val spoonacularScore: Double?, // 99.53563690185547
        @SerializedName("spoonacularSourceUrl")
        val spoonacularSourceUrl: String?, // https://spoonacular.com/cannellini-bean-and-asparagus-salad-with-mushrooms-782585
        @SerializedName("summary")
        val summary: String?, // Cannellini Bean and Asparagus Salad with Mushrooms requires approximately <b>45 minutes</b> from start to finish. This main course has <b>482 calories</b>, <b>31g of protein</b>, and <b>6g of fat</b> per serving. This gluten free, dairy free, lacto ovo vegetarian, and vegan recipe serves 6 and costs <b>$1.35 per serving</b>. 309 people were impressed by this recipe. Head to the store and pick up grain mustard, sea salt, lemon zest, and a few other things to make it today. It is brought to you by foodandspice.blogspot.com. Taking all factors into account, this recipe <b>earns a spoonacular score of 70%</b>, which is pretty good. Similar recipes are <a href="https://spoonacular.com/recipes/cannellini-bean-salad-422994">Cannellini Bean Salad</a>, <a href="https://spoonacular.com/recipes/refreshing-cannellini-bean-salad-113127">Refreshing Cannellini Bean Salad</a>, and <a href="https://spoonacular.com/recipes/cannellini-and-green-bean-salad-33177">Cannellini-and-Green Bean Salad</a>.
        @SerializedName("sustainable")
        val sustainable: Boolean?, // false
        @SerializedName("title")
        val title: String?, // Cannellini Bean and Asparagus Salad with Mushrooms
        @SerializedName("vegan")
        val vegan: Boolean?, // true
        @SerializedName("vegetarian")
        val vegetarian: Boolean?, // true
        @SerializedName("veryHealthy")
        val veryHealthy: Boolean?, // true
        @SerializedName("veryPopular")
        val veryPopular: Boolean?, // false
        @SerializedName("weightWatcherSmartPoints")
        val weightWatcherSmartPoints: Int? // 12
    ) : Parcelable {
        @Parcelize
        data class AnalyzedInstruction(
            @SerializedName("name")
            val name: String?,
            @SerializedName("steps")
            val steps: @RawValue List<Step>?
        ): Parcelable {
          data class Step(
                @SerializedName("equipment")
                val equipment:  List<Equipment?>?,
                @SerializedName("ingredients")
                val ingredients:   List<Ingredient?>?,
                @SerializedName("length")
                val length:  Length?,
                @SerializedName("number")
                val number: Int?, // 1
                @SerializedName("step")
                val step: String? // Rinse the cannellini beans and soak for 8 hours or overnight in several inches of water.
            )  {
                data class Equipment(
                    @SerializedName("id")
                    val id: Int?, // 404669
                    @SerializedName("image")
                    val image: String?, // sauce-pan.jpg
                    @SerializedName("localizedName")
                    val localizedName: String?, // sauce pan
                    @SerializedName("name")
                    val name: String? // sauce pan
                )

                data class Ingredient(
                    @SerializedName("id")
                    val id: Int?, // 10716050
                    @SerializedName("image")
                    val image: String?, // cooked-cannellini-beans.png
                    @SerializedName("localizedName")
                    val localizedName: String?, // cannellini beans
                    @SerializedName("name")
                    val name: String? // cannellini beans
                )

                data class Length(
                    @SerializedName("number")
                    val number: Int?, // 480
                    @SerializedName("unit")
                    val unit: String? // minutes
                )
            }
        }

        data class Nutrition(
            @SerializedName("caloricBreakdown")
            val caloricBreakdown: CaloricBreakdown?,
            @SerializedName("flavonoids")
            val flavonoids: List<Flavonoid?>?,
            @SerializedName("ingredients")
            val ingredients: List<Ingredient?>?,
            @SerializedName("nutrients")
            val nutrients: List<Nutrient?>?,
            @SerializedName("properties")
            val properties: List<Property?>?,
            @SerializedName("weightPerServing")
            val weightPerServing: WeightPerServing?
        ) {
            data class CaloricBreakdown(
                @SerializedName("percentCarbs")
                val percentCarbs: Double?, // 63.39
                @SerializedName("percentFat")
                val percentFat: Double?, // 11.41
                @SerializedName("percentProtein")
                val percentProtein: Double? // 25.2
            )

            data class Flavonoid(
                @SerializedName("amount")
                val amount: Double?, // 0.0
                @SerializedName("name")
                val name: String?, // Cyanidin
                @SerializedName("unit")
                val unit: String?
            )

            data class Ingredient(
                @SerializedName("amount")
                val amount: Double?, // 0.63
                @SerializedName("id")
                val id: Int?, // 10016049
                @SerializedName("name")
                val name: String?, // cannellini beans
                @SerializedName("nutrients")
                val nutrients: List<Nutrient?>?,
                @SerializedName("unit")
                val unit: String? // cups
            ) {
                data class Nutrient(
                    @SerializedName("amount")
                    val amount: Double?, // 489.85
                    @SerializedName("name")
                    val name: String?, // Folate
                    @SerializedName("percentOfDailyNeeds")
                    val percentOfDailyNeeds: Double?, // 137.1
                    @SerializedName("unit")
                    val unit: String? // µg
                )
            }

            data class Nutrient(
                @SerializedName("amount")
                val amount: Double?, // 488.93
                @SerializedName("name")
                val name: String?, // Calories
                @SerializedName("percentOfDailyNeeds")
                val percentOfDailyNeeds: Double?, // 24.45
                @SerializedName("unit")
                val unit: String? // kcal
            )

            data class Property(
                @SerializedName("amount")
                val amount: Double?, // 38.0
                @SerializedName("name")
                val name: String?, // Glycemic Index
                @SerializedName("unit")
                val unit: String?
            )

            data class WeightPerServing(
                @SerializedName("amount")
                val amount: Int?, // 227
                @SerializedName("unit")
                val unit: String? // g
            )
        }
    }
}