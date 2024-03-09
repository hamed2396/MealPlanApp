package com.example.mealplan.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crazylegend.kotlinextensions.force
import com.example.mealplan.data.db.entity.DailyMealEntity
import com.example.mealplan.data.db.entity.WeeklyMealEntity
import com.example.mealplan.data.models.ResponseWeeklyMeals
import com.example.mealplan.data.repository.PlanRepository
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.network.NetworkResponse
import com.example.mealplan.utils.network.NetworkStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class PlanViewModel @Inject constructor(
    private val repository: PlanRepository,
    private val networkResponse: NetworkResponse
) : ViewModel() {


    fun deleteDailyToDb(entity: DailyMealEntity) = viewModelScope.launch {
        repository.deleteDailyMeal(entity)
    }

    private val _mealDelete = MutableLiveData<List<DailyMealEntity>>()
    val mealDelete get() = _mealDelete.force<LiveData<List<DailyMealEntity>>>()
    fun observeMealDelete(title: String):String{
        _mealDelete.postValue(repository.getDailyMealsByTime(title))
        return title
    }


    fun getMealByTime(time: String) = repository.getDailyMealsByTime(time)

    val getAllMeals = MutableLiveData<MutableList<DailyMealEntity>>()
    fun getAllMeals() = viewModelScope.launch {
        repository.getAllMeals().collect {
            getAllMeals.value = it
        }
    }

    //weekly api
    private val _weekMeals = MutableLiveData<NetworkStatus<ResponseWeeklyMeals>>()
    val weekMeals get() = _weekMeals.force<LiveData<NetworkStatus<ResponseWeeklyMeals>>>()
    fun callWeeklyMeals() = viewModelScope.launch {
        _weekMeals.postValue(NetworkStatus.Loading())
        try {
            val response = repository.getWeeklyMeal()
            _weekMeals.postValue(networkResponse.handleResponse(response))
        } catch (_: Exception) {
            _weekMeals.postValue(NetworkStatus.Error(Constants.CHECK_CONNECTION))
        }
    }
    //weekly database

    private val _insertToDb = MutableLiveData<NetworkStatus<Unit>>()
    val insertToDb get() = _insertToDb.force<LiveData<NetworkStatus<Unit>>>()
    fun insertPlanToDb(entity: WeeklyMealEntity) = viewModelScope.launch {
        _insertToDb.postValue(NetworkStatus.Loading())
        try {
            val data = repository.insertWeeklyMeal(entity)
            _insertToDb.postValue(NetworkStatus.Success(data))
        } catch (_: Exception) {
            _insertToDb.postValue(NetworkStatus.Error(Constants.Insert_DB))
        }
    }

    private val _deleteWeeklyPlan = MutableLiveData<NetworkStatus<Unit>>()
    val deleteWeeklyPlan get() = _deleteWeeklyPlan.force<LiveData<NetworkStatus<Unit>>>()
    fun deleteWeeklyPlanDb() = viewModelScope.launch {
        _deleteWeeklyPlan.postValue(NetworkStatus.Loading())
        try {
            val data = repository.deleteAllWeeklyRecords()
            _deleteWeeklyPlan.postValue(NetworkStatus.Success(data))
        } catch (_: Exception) {
            _deleteWeeklyPlan.postValue(NetworkStatus.Error(Constants.Insert_DB))
        }
    }

    private val _weeklyItems = MutableLiveData<NetworkStatus<WeeklyMealEntity>>()
    val weeklyItems get() = _weeklyItems.force<LiveData<NetworkStatus<WeeklyMealEntity>>>()
    fun getAllWeeklyPlan() = viewModelScope.launch {
        _weeklyItems.postValue(NetworkStatus.Loading())
        try {
            val data = repository.getAllWeeklyMeals()
            _weeklyItems.postValue(NetworkStatus.Success(data))
        } catch (_: Exception) {
            _weeklyItems.postValue(NetworkStatus.Error(Constants.Insert_DB))
        }
    }

}



