package com.example.mealplan.ui.restaurant

import ResponseRestaurant
import Restaurant
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.crazylegend.kotlinextensions.isNull
import com.crazylegend.kotlinextensions.root.logError
import com.crazylegend.kotlinextensions.string.isNotNullOrEmpty
import com.example.mealplan.R
import com.example.mealplan.databinding.FragmentRestaurantInfoBinding
import com.example.mealplan.utils.base.BaseBottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantInfoFragment : BaseBottomSheetDialogFragment<FragmentRestaurantInfoBinding>(FragmentRestaurantInfoBinding::inflate) {
    private val args by navArgs<RestaurantInfoFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val info = args.info
            info.phoneNumber.toString().logError()
            if (info.address?.city.isNotNullOrEmpty()) {
                txtCity.text = info.address!!.city
            } else {
                getString(R.string.notIncluded)
            }
            if (info.address?.country.isNotNullOrEmpty()) {
                txtCountry.text = info.address!!.country
            } else {
                getString(R.string.notIncluded)
            }
            if (info.phoneNumber.isNull.not()) {
                txtNumber.text = info.phoneNumber.toString()
                txtNumber.setOnClickListener {
                    val number=info.phoneNumber.toString()
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$number")
                    }
                    startActivity(intent)
                }


            } else {
                txtNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                getString(R.string.notIncluded)
            }
            btnSubmit.setOnClickListener {
                val lat = info.address?.lat
                val long = info.address?.lon
                val uri = "geo:$lat,$long"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.setPackage("com.google.android.apps.maps")
                if (intent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.can_t_find_google_maps),
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        }

    }

    override fun getTheme() = R.style.RemoveDialogBackground

}