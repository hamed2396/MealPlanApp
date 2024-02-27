package com.example.mealplan.ui.unboadring

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect.EFFECT_HEAVY_CLICK
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.crazylegend.kotlinextensions.effects.vibrate
import com.crazylegend.kotlinextensions.ifFalse
import com.crazylegend.kotlinextensions.ifTrue
import com.crazylegend.kotlinextensions.regex.isEmail
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.snackbar
import com.crazylegend.kotlinextensions.views.textString
import com.crazylegend.kotlinextensions.views.visible
import com.example.mealplan.R
import com.example.mealplan.data.models.signup.BodySignUp
import com.example.mealplan.databinding.FragmentLoginBinding
import com.example.mealplan.utils.SessionManager
import com.example.mealplan.utils.base.BaseFragment
import com.example.mealplan.utils.extensions.infiniteSnackBar
import com.example.mealplan.utils.extensions.launchLifeCycleScope
import com.example.mealplan.utils.extensions.loadImage
import com.example.mealplan.utils.network.NetworkStatus
import com.example.mealplan.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel by viewModels<SignUpViewModel>()

    @Inject
    lateinit var body: BodySignUp

    @Inject
    lateinit var sessionManager: SessionManager

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            imgBack.load(R.drawable.bg_signup)
            btnSubmit.setOnClickListener {
                checkInputs().ifTrue {
                    requireContext().vibrate(200, EFFECT_HEAVY_CLICK)
                    isNetworkAvailable.ifTrue {
                        viewModel.postSignUp(body)

                    }
                }.ifFalse {

                    requireContext().vibrate(250)
                }
            }

            //load data
            loadSignUpData()

        }
    }

    private fun checkInputs(): Boolean {
        binding.apply {
            edtUsername.text.toString().ifEmpty {
                usernameLay.error = getString(R.string.can_t_be_empty)
            }
            edtLastname.text.toString().ifEmpty {
                lastnameLay.error = getString(R.string.can_t_be_empty)
            }
            edtFirstname.text.toString().ifEmpty {
                firstNameLay.error = getString(R.string.can_t_be_empty)
            }
            edtEmail.text.toString().ifEmpty {
                emailLay.error = getString(R.string.can_t_be_empty)
            }
            edtEmail.text.isEmail.ifFalse { emailLay.error = getString(R.string.nor_email) }

            edtUsername.addTextChangedListener {
                it.toString().isNotEmpty().ifTrue { usernameLay.error = null }
            }
            edtLastname.addTextChangedListener {
                it.toString().isNotEmpty().ifTrue { lastnameLay.error = null }
            }
            edtFirstname.addTextChangedListener {
                it.toString().isNotEmpty().ifTrue { firstNameLay.error = null }
            }
            edtEmail.addTextChangedListener {
                it.toString().isNotEmpty().ifTrue { emailLay.error = null }
            }
            body.email = edtEmail.textString
            body.firstName = edtFirstname.textString
            body.lastName = edtLastname.textString
            body.username = edtUsername.textString
            return edtEmail.text!!.isNotEmpty()
                    && edtFirstname.text!!.isNotEmpty()
                    && edtLastname.text!!.isNotEmpty()
                    && edtUsername.text!!.isNotEmpty()
                    && edtEmail.text.isEmail
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun loadSignUpData() {
        binding.apply {
            viewModel.signUp.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkStatus.Error -> {
                        root.infiniteSnackBar(
                            it.error!!,
                            actionName = getString(R.string.retry),
                            action = { viewModel.postSignUp(body) },
                            actionColor = R.color.mayaBlue
                        )
                        loading.apply {
                            gone()
                            cancelAnimation()

                        }
                        btnSubmit.visible()
                    }

                    is NetworkStatus.Loading -> {
                        btnSubmit.gone()
                        loading.apply {
                            visible()
                            playAnimation()
                        }

                    }

                    is NetworkStatus.Success -> {

                        loading.apply {
                            gone()
                            cancelAnimation()

                        }
                        launchLifeCycleScope {
                            sessionManager.saveUser(it.success?.username!!, it.success.hash!!)
                            findNavController().popBackStack()
                            findNavController().navigate(R.id.actionToNavMain)
                        }

                    }


                }
            }
        }
    }


}

