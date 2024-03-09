package com.example.mealplan.utils.extensions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.view.WindowCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.request.CachePolicy
import com.airbnb.lottie.LottieAnimationView
import com.crazylegend.kotlinextensions.context.color
import com.crazylegend.kotlinextensions.views.color
import com.crazylegend.kotlinextensions.views.colorStateList
import com.crazylegend.kotlinextensions.views.textColor
import com.example.mealplan.R
import com.example.mealplan.databinding.ChangePlanMealBinding
import com.example.mealplan.databinding.ItemNutritionTooltipBinding
import com.example.mealplan.databinding.PlanMealLayBinding
import com.example.mealplan.utils.Constants
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.showAlignBottom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat

fun Fragment.launchLifeCycleScope(scope: suspend CoroutineScope.() -> Unit) =
    lifecycleScope.launch { scope() }

val Int.commaSeparator: String get() = DecimalFormat("#,###").format(this)

fun ImageView.changeTint(@ColorRes color: Int) =
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color(color)))

fun FragmentActivity.statusBarIconColor(dark: Boolean) {
    window.apply {

        WindowCompat.getInsetsController(this, this.decorView).apply {
            isAppearanceLightStatusBars = dark
        }
    }
}

fun LottieAnimationView.onAnimationStartOrEnd(start: () -> Unit = {}, end: () -> Unit) {
    addAnimatorListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            start()
        }

        override fun onAnimationEnd(animation: Animator) {
            end()
        }

        override fun onAnimationCancel(animation: Animator) {

        }

        override fun onAnimationRepeat(animation: Animator) {

        }
    })
}

infix fun <T> T.isBiggerThan(that: T): Boolean where T : Comparable<T>, T : Number = this > that
infix fun <T> T.isLessThan(that: T): Boolean where T : Comparable<T>, T : Number = this < that

val String.toEditable: Editable get() = Editable.Factory.getInstance().newEditable(this)

fun ImageView.loadImage(url: String) {
    load(url) {
        crossfade(true)
        crossfade(500)
        diskCachePolicy(CachePolicy.ENABLED)
        placeholder(R.drawable.placeholder)
    }
}

@SuppressLint("SetTextI18n")
fun View.showToolTip(title: String, amount: String) {
    val color: Int
    val inflater = LayoutInflater.from(context)
    val binding = ItemNutritionTooltipBinding.inflate(inflater)
    binding.txtTitle.text = "$title\n $amount g"
    when (title) {
        Constants.FAT -> {
            binding.txtTitle.textColor = color(R.color.white)
            color = R.color.royalOrange

        }

        Constants.PROTEIN -> {
            binding.txtTitle.textColor = color(R.color.white)
            color = R.color.violetsAreBlue
        }

        else -> {
            binding.txtTitle.textColor = color(R.color.white)
            color = R.color.pastelRed
        }
    }

    Balloon.Builder(context).apply {
        setSize(90, 90)
        setLayout(binding)
        setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
        setArrowSize(10)
        setArrowPosition(0.5f)
        setPadding(12)
        setCornerRadius(8f)
        setBackgroundColorResource(color)
        setBalloonAnimation(BalloonAnimation.ELASTIC)
        setLifecycleOwner(findViewTreeLifecycleOwner())
        build().also { showAlignBottom(it) }
    }
}

fun View.showPlanMenu(layoutClicked: (String) -> Unit) {
    val binding: PlanMealLayBinding = PlanMealLayBinding.inflate(LayoutInflater.from(context))
    val balloon = Balloon.Builder(context)
        .setLayout(binding.root)
        .setArrowSize(8)
        .setArrowColor(color(R.color.limedSpruce))
        .setArrowPosition(0.8f)
        .setBackgroundColor(color(R.color.limedSpruce))
        .setCornerRadius(8f)
        .setBalloonAnimation(BalloonAnimation.ELASTIC)
        .setLifecycleOwner(findViewTreeLifecycleOwner())
        .build()

    binding.breakfast.setOnClickListener {
        layoutClicked(Constants.BREAKFAST)
        balloon.dismiss()
    }

    binding.lunch.setOnClickListener {
        layoutClicked(Constants.LUNCH)
        balloon.dismiss()
    }

    binding.dinner.setOnClickListener {
        layoutClicked(Constants.DINNER)
        balloon.dismiss()
    }

    balloon.showAlignBottom(this)
}


fun View.infiniteSnackBar(
    title: String,
    actionName: String,
    action: () -> Unit,
    @ColorRes actionColor: Int,
    swipeDismiss: Boolean = false
) {
    val snackBar = Snackbar.make(this, title, Snackbar.LENGTH_INDEFINITE)
        .setAction(actionName) { action() }
        .setActionTextColor(context.color(actionColor))

    // Set behavior to prevent swipe dismissal
    val noSwipeSnackBar = object : BaseTransientBottomBar.Behavior() {
        override fun canSwipeDismissView(child: View): Boolean {
            return swipeDismiss
        }
    }
    snackBar.behavior = noSwipeSnackBar

    snackBar.show()
}
fun View.showChangePlan(changePlan:Balloon.() -> Unit) {
    val binding: ChangePlanMealBinding = ChangePlanMealBinding.inflate(LayoutInflater.from(context))
    val balloon = Balloon.Builder(context)
        .setLayout(binding.root)
        .setArrowSize(8)
        .setArrowColor(color(R.color.limedSpruce))
        .setArrowPosition(.9f)
        .setBackgroundColor(color(R.color.limedSpruce))
        .setCornerRadius(8f)
        .setMarginRight(8)
        .setBalloonAnimation(BalloonAnimation.FADE)
        .setLifecycleOwner(findViewTreeLifecycleOwner())
        .build()
    binding.btnChange.setOnClickListener { changePlan(balloon) }
    binding.btnCancel.setOnClickListener { balloon.dismiss() }
    balloon.showAlignBottom(this)
}

    fun ChipGroup.setupChip(list: MutableList<String>) {
        list.forEach {
            val state = ColorStateList(
                arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
                intArrayOf(color(R.color.white), color(R.color.limedSpruce))
            )
            val drawable = ChipDrawable.createFromAttributes(context, null, 0, R.style.greenChip)
            Chip(context).also { chip ->
                chip.setChipDrawable(drawable)
                chip.text = it
                chip.setTextColor(state)
                chip.textSize = 16f
                chip.tag = it
                chip.chipStrokeWidth = .7f
                chip.chipStrokeColor = colorStateList(R.color.grayAlpha)
                addView(chip)
            }
        }
    }


    fun ChipGroup.autoSelectChip(chipText: String?) {
        for (i in 0 until childCount) {
            val chip = getChildAt(i) as? Chip
            if (chip != null && chipText != null && chip.text == chipText) {
                chip.isChecked = true
                break
            }
        }
    }

    fun TextView.fade(fadeIn: Boolean) {
        val fade: ObjectAnimator
        if (fadeIn) {
            fade = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
            fade.duration = 400 // Adjust the duration as needed

            fade.start()
        } else {
            fade = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
            fade.duration = 400 // Adjust the duration as needed
            fade.start()
        }

    }

    fun Int.minToHour(): String {
        val time: String
        val hours: Int = this / 60
        val minutes: Int = this % 60
        time = if (hours > 0) "${hours}h:${minutes}min" else "${minutes}min"
        return time
    }

