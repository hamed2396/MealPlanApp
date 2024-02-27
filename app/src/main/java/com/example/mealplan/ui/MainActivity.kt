package com.example.mealplan.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.viewbinding.viewBinding
import com.example.mealplan.R
import com.example.mealplan.databinding.ActivityMainBinding
import com.example.mealplan.utils.extensions.statusBarIconColor
import dagger.hilt.android.AndroidEntryPoint
import io.github.inflationx.viewpump.ViewPumpContextWrapper

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val navHost by lazy { supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(binding.root)

        binding.apply {
            bottomNav.apply {
                background = null
                setupWithNavController(navHost.navController)
                setOnItemReselectedListener { }
            }
           // applyInsets()
            navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.splashFragment -> {
                        fabMenu.gone()
                        bottomAppBar.gone()
                    }

                    R.id.viewPagerFragment -> {
                        fabMenu.gone()
                        bottomAppBar.gone()
                    }
                    R.id.restaurantFragment -> {
                        fabMenu.gone()
                        bottomAppBar.gone()
                    }
                    R.id.detailFragment -> {
                        fabMenu.gone()
                        bottomAppBar.gone()
                    }


                    else -> {
                        fabMenu.visible()
                        bottomAppBar.visible()
                    }
                }
            }
        }
        statusBarIconColor(true)


    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))

    }
}