package com.example.mealplan.utils.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.crazylegend.kotlinextensions.activity.rootView
import com.crazylegend.kotlinextensions.ifFalse
import com.crazylegend.kotlinextensions.ifNull
import com.crazylegend.kotlinextensions.views.snackbar
import com.example.mealplan.R
import com.example.mealplan.utils.extensions.launchLifeCycleScope
import com.example.mealplan.utils.network.NetworkChecker
import javax.inject.Inject


abstract class BaseFragment<VB : ViewBinding>(private val bindingInflater: (LayoutInflater) -> VB) :
    Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!
    protected var isNetworkAvailable = false

    @Inject
    lateinit var networkChecker: NetworkChecker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater(inflater)
        _binding.ifNull { throw IllegalArgumentException(getString(R.string.binding)) }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchLifeCycleScope {
            networkChecker.checkNetwork().collect {
                isNetworkAvailable = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}