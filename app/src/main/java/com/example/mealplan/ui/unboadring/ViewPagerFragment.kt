package com.example.mealplan.ui.unboadring

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.crazylegend.kotlinextensions.activity.setStatusBarColor
import com.crazylegend.kotlinextensions.context.color
import com.crazylegend.kotlinextensions.context.isShowKeyboard
import com.crazylegend.kotlinextensions.views.bottomMargin
import com.crazylegend.kotlinextensions.views.snackbarLong
import com.example.mealplan.R
import com.example.mealplan.databinding.FragmentViewPagerBinding
import com.example.mealplan.utils.Constants.DOUBLE_BACK_PRESS_DELAY
import com.example.mealplan.utils.Constants.MAX_BACK_PRESS_COUNT
import com.example.mealplan.utils.OnBoardingViewPager
import com.example.mealplan.utils.base.BaseFragment
import com.example.mealplan.utils.extensions.launchLifeCycleScope
import com.example.mealplan.utils.extensions.statusBarIconColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class ViewPagerFragment :
    BaseFragment<FragmentViewPagerBinding>(FragmentViewPagerBinding::inflate) {
    @Inject
    lateinit var onBoardingViewPager: OnBoardingViewPager
    private var backPress = 0
    private lateinit var callback: OnBackPressedCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().apply {
            setStatusBarColor(color(R.color.greenLizard))
            statusBarIconColor(false)
        }
        binding.apply {
            viewPager.apply {
                adapter = onBoardingViewPager
                //create fragment behind the scene .its for user not seeing lottie anim getting created while navigating
                //-1 because it don't need to preRender signUp fragment
                offscreenPageLimit = onBoardingViewPager.itemCount.minus(1)
            }
            dotsIndicator.setViewPager2(viewPager)

            controlBackPress()
        }

    }

    private fun controlBackPress() {
         callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.viewPager.apply {
                    when (currentItem) {
                        0 -> {
                            requireActivity().isShowKeyboard()
                            backPress += 1
                            if (backPress == 1) {
                                binding.root.snackbarLong(getString(R.string.click_again_to_exit))
                                launchLifeCycleScope {
                                    delay(DOUBLE_BACK_PRESS_DELAY)
                                    backPress = 0
                                }
                            } else if (backPress == MAX_BACK_PRESS_COUNT) {
                                requireActivity().finish()
                            }
                        }

                        1 -> {
                            currentItem = 0
                        }

                        2 -> {
                            currentItem = 1
                        }
                    }
                }
            }
        }

        // Add the callback to the back press dispatcher
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }




    override fun onDestroy() {
        super.onDestroy()
        callback.remove()
    }


}