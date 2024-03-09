package com.example.mealplan.utils

import android.content.Context
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.mealplan.data.db.MealAppDataBase
import com.example.mealplan.data.db.MealTypeConverters
import com.example.mealplan.utils.events.Event
import com.example.mealplan.utils.events.EventBus
import com.google.gson.Gson

class DeleteDataBaseWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {

        val gson = Gson()
        val dataStore = WeeklyPlanStore(applicationContext)
        val dataBase =
            Room.databaseBuilder(applicationContext, MealAppDataBase::class.java, Constants.DB_NAME)
                .fallbackToDestructiveMigration().allowMainThreadQueries()
                .addTypeConverter(MealTypeConverters(gson)).build()
        // Delete records from the database
        dataBase.dao().deleteAllRecords()
        dataStore.clearAll()

        dataBase.close()

        //Publish event to indicate completion
        EventBus.publish(Event.DoneWorker)


        return Result.success()


    }
}

