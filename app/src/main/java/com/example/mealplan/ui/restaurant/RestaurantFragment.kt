package com.example.mealplan.ui.restaurant

import Restaurant
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.crazylegend.kotlinextensions.string.isNotNullOrEmpty
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.recyclerview.initRecyclerViewAdapter
import com.example.mealplan.R
import com.example.mealplan.adapters.meal_fragment.RestaurantAdapter
import com.example.mealplan.databinding.FragmentSearchBinding
import com.example.mealplan.databinding.RestauranFragmentBinding
import com.example.mealplan.utils.base.BaseFragment
import com.example.mealplan.utils.extensions.infiniteSnackBar
import com.example.mealplan.utils.network.NetworkStatus
import com.example.mealplan.viewmodel.MealViewModel
import com.example.mealplan.viewmodel.RestaurantViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RestaurantFragment : BaseFragment<RestauranFragmentBinding>(RestauranFragmentBinding::inflate) {
    private val viewModel by viewModels<RestaurantViewModel>()
    @Inject
    lateinit var restaurantAdapter: RestaurantAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel.getRestaurant()
            loaRestaurantData()
        }
    }
     private fun loaRestaurantData() {
       binding.apply {
           viewModel.restaurantData.observe(viewLifecycleOwner) {
               when (it) {
                   is NetworkStatus.Error -> {
                       root.infiniteSnackBar(
                           it.error!!,
                           actionName = getString(R.string.retry),
                           action = { viewModel.getRestaurant() },
                           actionColor = R.color.mayaBlue
                       )
                       restaurantList.hideShimmer()
                   }

                   is NetworkStatus.Loading -> {
                       restaurantList.showShimmer()

                   }

                   is NetworkStatus.Success -> {
                       initRecyclerView(it.success!!.restaurants!!)

                   }
               }
           }
       }
   }
    private fun initRecyclerView(data: List<Restaurant>) {
       binding.apply {
           restaurantAdapter.setData(data)
           restaurantList.initRecyclerViewAdapter(
               restaurantAdapter
           )
           restaurantAdapter.setOnItemClickListener {
               RestaurantFragmentDirections.actionRestaurantToResInfo(it).apply {
                   findNavController().navigate(this)
               }
           }
       }
   }
}