package com.example.mealplan.utils

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.example.mealplan.utils.base.BaseBottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


open class FullHeightBottomSheet<VB : ViewBinding > (bindingInflater:(LayoutInflater)->VB):
    BaseBottomSheetDialogFragment<VB>(bindingInflater) {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {dialogInterface->

            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let {
                val behaviour = BottomSheetBehavior.from(it)
               // setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }


}