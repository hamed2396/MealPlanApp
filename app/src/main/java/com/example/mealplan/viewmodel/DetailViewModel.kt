package com.example.mealplan.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.kotlinextensions.force
import com.example.mealplan.data.db.entity.DailyMealEntity
import com.example.mealplan.data.models.detail.ResponseDetailInfo
import com.example.mealplan.data.models.detail.ResponseDetailVideo
import com.example.mealplan.data.models.detail.ResponseNutrition
import com.example.mealplan.data.models.detail.ResponseSimilar
import com.example.mealplan.data.repository.DetailRepository
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.network.NetworkResponse
import com.example.mealplan.utils.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: DetailRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {
    private val _mealInfo = MutableLiveData<NetworkStatus<ResponseDetailInfo>>()
    val mealInfo get() = _mealInfo.force<LiveData<NetworkStatus<ResponseDetailInfo>>>()


    fun callMealInformation(id: Int) = viewModelScope.launch {
        _mealInfo.postValue(NetworkStatus.Loading())
        try {
            val response = repository.getMealInfo(id)
            _mealInfo.postValue(networkResponse.handleResponse(response))
        } catch (_: Exception) {
            _mealInfo.postValue(NetworkStatus.Error(Constants.CHECK_CONNECTION))
        }
    }

    private val _mealVideo = MutableLiveData<NetworkStatus<ResponseDetailVideo>>()
    val mealVideo get() = _mealVideo.force<LiveData<NetworkStatus<ResponseDetailVideo>>>()
    fun callMealVideo(title: String) = viewModelScope.launch {
        _mealVideo.postValue(NetworkStatus.Loading())
        try {
            val response = repository.getMealVideo(title)
            _mealVideo.postValue(networkResponse.handleResponse(response))
        } catch (_: Exception) {
            _mealVideo.postValue(NetworkStatus.Error(Constants.CHECK_CONNECTION))
        }
    }

    private val _mealNutrition = MutableLiveData<NetworkStatus<ResponseNutrition>>()
    val mealNutrition get() = _mealNutrition.force<LiveData<NetworkStatus<ResponseNutrition>>>()
    fun callMealNutrition(id: Int) = viewModelScope.launch {
        _mealNutrition.postValue(NetworkStatus.Loading())
        try {
            val response = repository.getMealNutrition(id)
            _mealNutrition.postValue(networkResponse.handleResponse(response))
        } catch (_: Exception) {
            _mealNutrition.postValue(NetworkStatus.Error(Constants.CHECK_CONNECTION))
        }
    }

    private fun saveResponseBodyToFile(context: Context, responseBody: ResponseBody): File {
        // Save the response body to a file
        val file = File(context.getExternalFilesDir(null), "image.png")
        val inputStream = responseBody.byteStream()
        FileOutputStream(file).use { outputStream ->
            inputStream.use { input ->
                input.copyTo(outputStream)
            }
        }
        return file
    }

    suspend fun downloadPngFile(context: Context, id: Int): NetworkStatus<File> {
        return withContext(Dispatchers.IO) {
            try {
                val response = repository.getMealNutritionChart(id)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val downloadedFile = saveResponseBodyToFile(context, responseBody)
                        NetworkStatus.Success(downloadedFile)
                    } else {
                        NetworkStatus.Error("Empty response body")
                    }
                } else {
                    NetworkStatus.Error("Failed to download Chart file")
                }
            } catch (e: Exception) {
                NetworkStatus.Error("Failed to download Chart file")
            }
        }
    }

    private val _similarRecipe = MutableLiveData<NetworkStatus<ResponseSimilar>>()
    val similarRecipe get() = _similarRecipe.force<LiveData<NetworkStatus<ResponseSimilar>>>()
    fun callSimilarRecipe(id: Int) = viewModelScope.launch {
        _similarRecipe.postValue(NetworkStatus.Loading())
        try {
            val response = repository.getSimilar(id)
            _similarRecipe.postValue(networkResponse.handleResponse(response))
        } catch (_: Exception) {
            _similarRecipe.postValue(NetworkStatus.Error("Unable to Load Similar List"))
        }
    }


    //database
    fun insertDailyToDb(entity: DailyMealEntity) = viewModelScope.launch {
        repository.insertDailyMeal(entity)
    }

    fun deleteDailyToDb(entity: DailyMealEntity) = viewModelScope.launch {
        repository.deleteDailyMeal(entity)
    }

    fun getMealByTime(time: String) = repository.getDailyMealsByTime(time)

    fun existMeal(id: Int) = repository.existMeal(id)
    fun getSingleMealById(id: Int) = repository.getSingleMealById(id)

    val getAllMeals = MutableLiveData<List<DailyMealEntity>>()
     fun getAllMeals() = viewModelScope.launch {
        repository.getAllMeals().collect {

                getAllMeals.postValue(it)


        }
    }


}


