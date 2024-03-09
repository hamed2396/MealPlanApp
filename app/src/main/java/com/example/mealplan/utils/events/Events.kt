package com.example.mealplan.utils.events

import com.example.mealplan.data.db.entity.DailyMealEntity
import com.example.mealplan.data.models.search.SearchFilterModel

sealed class Event() {
    class HasFilterApplied(val model:SearchFilterModel):Event()
    object DoneWorker : Event()
    class CancelWorker(val list:List<DailyMealEntity>?) : Event()
}