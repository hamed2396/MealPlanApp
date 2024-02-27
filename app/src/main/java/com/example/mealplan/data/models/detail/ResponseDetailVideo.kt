package com.example.mealplan.data.models.detail


import com.google.gson.annotations.SerializedName

data class ResponseDetailVideo(
    @SerializedName("totalResults")
    val totalResults: Int?, // 18
    @SerializedName("videos")
    val videos: List<Video?>?
) {
    data class Video(
        @SerializedName("length")
        val length: Int?, // 333
        @SerializedName("rating")
        val rating: Double?, // 1.0
        @SerializedName("shortTitle")
        val shortTitle: String?, // Macaroni salad
        @SerializedName("thumbnail")
        val thumbnail: String?, // https://i.ytimg.com/vi/81bn4p8H3Kg/mqdefault.jpg
        @SerializedName("title")
        val title: String?, // Macaroni salad - pasta salad recipes - healthy recipe channel - quick tasty recipe - cooking channel
        @SerializedName("views")
        val views: Int?, // 307
        @SerializedName("youTubeId")
        val youTubeId: String? // 81bn4p8H3Kg
    )
}