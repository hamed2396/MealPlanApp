package com.example.mealplan.data.models


import com.google.gson.annotations.SerializedName

data class ResponseWeeklyMeals(
    @SerializedName("week")
    val week: Week?
) {
    data class Week(
        @SerializedName("friday")
        val friday: Friday,
        @SerializedName("monday")
        val monday: Monday,
        @SerializedName("saturday")
        val saturday: Saturday,
        @SerializedName("sunday")
        val sunday: Sunday,
        @SerializedName("thursday")
        val thursday: Thursday,
        @SerializedName("tuesday")
        val tuesday: Tuesday,
        @SerializedName("wednesday")
        val wednesday: Wednesday
    ) {
        data class Friday(
            @SerializedName("meals")
            val meals: List<Meal>,
            @SerializedName("nutrients")
            val nutrients: Nutrients
        ) {
            data class Meal(
                @SerializedName("id")
                val id: Int?, // 640767
                @SerializedName("imageType")
                val imageType: String?, // jpg
                @SerializedName("readyInMinutes")
                val readyInMinutes: Int?, // 45
                @SerializedName("servings")
                val servings: Int?, // 4
                @SerializedName("sourceUrl")
                val sourceUrl: String?, // https://spoonacular.com/crepes-suzette-640767
                @SerializedName("title")
                val title: String? // Crepes Suzette
            )

            data class Nutrients(
                @SerializedName("calories")
                val calories: Double?, // 1874.68
                @SerializedName("carbohydrates")
                val carbohydrates: Double?, // 268.27
                @SerializedName("fat")
                val fat: Double?, // 67.56
                @SerializedName("protein")
                val protein: Double? // 49.64
            )
        }

        data class Monday(
            @SerializedName("meals")
            val meals: List<Meal>,
            @SerializedName("nutrients")
            val nutrients: Nutrients?
        ) {
            data class Meal(
                @SerializedName("id")
                val id: Int?, // 664280
                @SerializedName("imageType")
                val imageType: String?, // jpg
                @SerializedName("readyInMinutes")
                val readyInMinutes: Int?, // 45
                @SerializedName("servings")
                val servings: Int?, // 8
                @SerializedName("sourceUrl")
                val sourceUrl: String?, // https://spoonacular.com/vanilla-bean-cherry-granola-bars-664280
                @SerializedName("title")
                val title: String? // Vanilla Bean Cherry Granola Bars
            )

            data class Nutrients(
                @SerializedName("calories")
                val calories: Double?, // 1911.29
                @SerializedName("carbohydrates")
                val carbohydrates: Double?, // 282.7
                @SerializedName("fat")
                val fat: Double?, // 66.58
                @SerializedName("protein")
                val protein: Double? // 53.43
            )
        }

        data class Saturday(
            @SerializedName("meals")
            val meals: List<Meal>,
            @SerializedName("nutrients")
            val nutrients: Nutrients?
        ) {
            data class Meal(
                @SerializedName("id")
                val id: Int?, // 664280
                @SerializedName("imageType")
                val imageType: String?, // jpg
                @SerializedName("readyInMinutes")
                val readyInMinutes: Int?, // 45
                @SerializedName("servings")
                val servings: Int?, // 8
                @SerializedName("sourceUrl")
                val sourceUrl: String?, // https://spoonacular.com/vanilla-bean-cherry-granola-bars-664280
                @SerializedName("title")
                val title: String? // Vanilla Bean Cherry Granola Bars
            )

            data class Nutrients(
                @SerializedName("calories")
                val calories: Double?, // 1920.1
                @SerializedName("carbohydrates")
                val carbohydrates: Double?, // 281.48
                @SerializedName("fat")
                val fat: Double?, // 65.9
                @SerializedName("protein")
                val protein: Double? // 54.5
            )
        }

        data class Sunday(
            @SerializedName("meals")
            val meals: List<Meal>,
            @SerializedName("nutrients")
            val nutrients: Nutrients?
        ) {
            data class Meal(
                @SerializedName("id")
                val id: Int?, // 643450
                @SerializedName("imageType")
                val imageType: String?, // jpg
                @SerializedName("readyInMinutes")
                val readyInMinutes: Int?, // 45
                @SerializedName("servings")
                val servings: Int?, // 8
                @SerializedName("sourceUrl")
                val sourceUrl: String?, // https://spoonacular.com/fresh-cherry-scones-643450
                @SerializedName("title")
                val title: String? // Fresh Cherry Scones
            )

            data class Nutrients(
                @SerializedName("calories")
                val calories: Double?, // 1875.22
                @SerializedName("carbohydrates")
                val carbohydrates: Double?, // 282.25
                @SerializedName("fat")
                val fat: Double?, // 60.83
                @SerializedName("protein")
                val protein: Double? // 55.61
            )
        }

        data class Thursday(
            @SerializedName("meals")
            val meals: List<Meal>,
            @SerializedName("nutrients")
            val nutrients: Nutrients?
        ) {
            data class Meal(
                @SerializedName("id")
                val id: Int?, // 640767
                @SerializedName("imageType")
                val imageType: String?, // jpg
                @SerializedName("readyInMinutes")
                val readyInMinutes: Int?, // 45
                @SerializedName("servings")
                val servings: Int?, // 4
                @SerializedName("sourceUrl")
                val sourceUrl: String?, // https://spoonacular.com/crepes-suzette-640767
                @SerializedName("title")
                val title: String? // Crepes Suzette
            )

            data class Nutrients(
                @SerializedName("calories")
                val calories: Double?, // 1782.17
                @SerializedName("carbohydrates")
                val carbohydrates: Double?, // 260.92
                @SerializedName("fat")
                val fat: Double?, // 62.29
                @SerializedName("protein")
                val protein: Double? // 47.73
            )
        }

        data class Tuesday(
            @SerializedName("meals")
            val meals: List<Meal>,
            @SerializedName("nutrients")
            val nutrients: Nutrients
        ) {
            data class Meal(
                @SerializedName("id")
                val id: Int?, // 632614
                @SerializedName("imageType")
                val imageType: String?, // jpg
                @SerializedName("readyInMinutes")
                val readyInMinutes: Int?, // 45
                @SerializedName("servings")
                val servings: Int?, // 12
                @SerializedName("sourceUrl")
                val sourceUrl: String?, // https://spoonacular.com/apple-cherry-pear-and-almond-breakfast-muffins-632614
                @SerializedName("title")
                val title: String? // Apple, Cherry, Pear and Almond Breakfast Muffins
            )

            data class Nutrients(
                @SerializedName("calories")
                val calories: Double?, // 1997.06
                @SerializedName("carbohydrates")
                val carbohydrates: Double?, // 291.27
                @SerializedName("fat")
                val fat: Double?, // 66.26
                @SerializedName("protein")
                val protein: Double? // 59.09
            )
        }

        data class Wednesday(
            @SerializedName("meals")
            val meals: List<Meal>,
            @SerializedName("nutrients")
            val nutrients: Nutrients
        ) {
            data class Meal(
                @SerializedName("id")
                val id: Int?, // 645175
                @SerializedName("imageType")
                val imageType: String?, // jpg
                @SerializedName("readyInMinutes")
                val readyInMinutes: Int?, // 45
                @SerializedName("servings")
                val servings: Int?, // 18
                @SerializedName("sourceUrl")
                val sourceUrl: String?, // https://spoonacular.com/granola-645175
                @SerializedName("title")
                val title: String? // Granola
            )

            data class Nutrients(
                @SerializedName("calories")
                val calories: Double?, // 2015.29
                @SerializedName("carbohydrates")
                val carbohydrates: Double?, // 307.31
                @SerializedName("fat")
                val fat: Double?, // 60.29
                @SerializedName("protein")
                val protein: Double? // 57.11
            )
        }
    }
}