package com.example.mealplan.adapters.detail

import android.view.ViewTreeObserver
import androidx.core.widget.NestedScrollView
import com.google.android.material.tabs.TabLayout

class TabLayoutScrollHandler(
    private val tabLayout: TabLayout,
    private val scrollView: NestedScrollView
) {
    private var isUserTabClick = false

    init {
        setupListeners()
    }

    private fun setupListeners() {
        val onScrollChangedListener = ViewTreeObserver.OnScrollChangedListener {
            if (!isUserTabClick) {
                val scrollY = scrollView.scrollY
                val tabLayoutHeight = tabLayout.height
                val textView1Bottom = tabLayout.getTabAt(0)?.view?.bottom ?: 0
                val textView2Bottom = tabLayout.getTabAt(1)?.view?.bottom ?: 0
                val textView3Bottom = tabLayout.getTabAt(2)?.view?.bottom ?: 0

                // Check if the scroll position is within the range of each tab's content
                when {
                    scrollY < textView1Bottom - tabLayoutHeight / 2 -> selectTab(0)
                    scrollY < textView2Bottom - tabLayoutHeight / 2 -> selectTab(1)
                    scrollY < textView3Bottom - tabLayoutHeight / 2 -> selectTab(2)
                }
            }
        }

        val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                isUserTabClick = true
                scrollToView(tab.position)
                isUserTabClick = false
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        }

        scrollView.viewTreeObserver.addOnScrollChangedListener(onScrollChangedListener)
        tabLayout.addOnTabSelectedListener(onTabSelectedListener)
    }

    private fun selectTab(position: Int) {
        if (tabLayout.selectedTabPosition != position) {
            tabLayout.getTabAt(position)?.select()
        }
    }

    private fun scrollToView(position: Int) {
        val viewToScrollTo = when (position) {
            0 -> tabLayout.getTabAt(0)?.view
            1 -> tabLayout.getTabAt(1)?.view
            2 -> tabLayout.getTabAt(2)?.view
            else -> null
        }
        viewToScrollTo?.let { tabView ->
            val scrollY = tabView.bottom - scrollView.height
            scrollView.smoothScrollTo(0, scrollY)
        }
    }
}