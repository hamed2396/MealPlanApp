package com.example.mealplan.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.viewbinding.viewBinding
import com.example.mealplan.R
import com.example.mealplan.databinding.ActivityMainBinding
import com.example.mealplan.utils.Constants
import com.example.mealplan.utils.DeleteDataBaseWorker
import com.example.mealplan.utils.events.Event
import com.example.mealplan.utils.events.EventBus
import com.example.mealplan.utils.extensions.statusBarIconColor
import dagger.hilt.android.AndroidEntryPoint
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val navHost by lazy { supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
        setContentView(binding.root)
        binding.apply {
            cancelWorkManager()
            bottomNav.apply {

                setupWithNavController(navHost.navController)
                setOnItemReselectedListener { }

            }
            // applyInsets()
            navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.splashFragment -> {
                      bottomNav.gone()
                    }

                    R.id.viewPagerFragment -> {
                        bottomNav.gone()
                    }

                    R.id.restaurantFragment -> {
                        bottomNav.gone()
                    }

                    R.id.detailFragment -> {
                        bottomNav.gone()
                    }


                    else -> {
                        bottomNav.gone()
                    }
                }
            }
        }
        statusBarIconColor(true)


    }

        private fun cancelWorkManager() {
            lifecycleScope.launch {
                EventBus.subscribe<Event.CancelWorker>(this) {
                    if (it.list.isNullOrEmpty()) {
                        Constants.WORKER_ID?.let { id ->
                            "cancelled".logError()
                            WorkManager.getInstance(this@MainActivity).cancelWorkById(id)
                        }
                    }
                }
            }


        }


    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))

    }
}