package com.example.mealplan.ui.splash

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import coil.load
import com.crazylegend.kotlinextensions.activity.setStatusBarColor
import com.crazylegend.kotlinextensions.context.color
import com.crazylegend.kotlinextensions.fragments.compatColor
import com.crazylegend.kotlinextensions.string.isNotNullOrEmpty
import com.example.mealplan.BuildConfig
import com.example.mealplan.R
import com.example.mealplan.databinding.FragmentSplashBinding
import com.example.mealplan.utils.SessionManager
import com.example.mealplan.utils.base.BaseFragment
import com.example.mealplan.utils.extensions.launchLifeCycleScope
import com.example.mealplan.utils.extensions.onAnimationStartOrEnd
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    @Inject
    lateinit var sessionManager: SessionManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().setStatusBarColor(compatColor(R.color.greenLizard))
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            imgBack.load(R.drawable.bg_splash)
            animationView.onAnimationStartOrEnd {
                launchLifeCycleScope {
                    if (!BuildConfig.DEBUG) delay(1000)
                    sessionManager.getUser().firstOrNull {
                        findNavController().popBackStack(R.id.splashFragment, true)
                        if (it.username.isNotNullOrEmpty()) {
                            findNavController().navigate(R.id.actionToNavMain)
                        } else {
                            findNavController().navigate(R.id.actionSplashToViewPager)
                        }
                        false
                    }

                }
            }

        }
    }
}