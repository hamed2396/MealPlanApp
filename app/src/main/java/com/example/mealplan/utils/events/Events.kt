package com.example.mealplan.utils.events

import com.example.mealplan.data.models.search.SearchFilterModel

sealed class Event() {
    class HasFilterApplied(val model:SearchFilterModel):Event()
    class HasFilterCleared(val model:SearchFilterModel):Event()
}