package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.R
import com.example.nordside_mobile.databinding.FragmentRegisterBinding
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.ServerToken
import com.example.nordside_mobile.repository.Resource
import com.example.nordside_mobile.usecases.ValidateState
import com.example.nordside_mobile.viewmodel.FragmentRegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentRegister : Fragment(R.layout.fragment_register)  {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FragmentRegisterViewModel by viewModels()
    private var callbacks: Callback? = null


    interface Callback {
        fun onRegisterClicked()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRegisterBinding.bind(view)


        with(binding) {
            etEmail.setText(arguments?.get("EMAIL").toString())
            etPassword.setText(arguments?.get("PASSWORD").toString())

            buttonRegister.setOnClickListener { registerButtonListener() }
        }
    }

    private fun registerButtonListener() {
        val loginBody = LoginBody(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        showProgressBar()

        viewModel.viewModelScope.launch {
            val validateState = viewModel.loginBodyChecker(loginBody)

            if (validateState.emailState == ValidateState.EmailState.OK
                && validateState.passwordState == ValidateState.PasswordState.OK
            ) {

                // Todo: реализовать регистрацию на сервере.
                val registerStatusResource = viewModel.register(loginBody)
                hideProgressBar()
                if (registerStatusResource is Resource.Success) {
                    Log.d("RegisterFragment", "Resource.Success ${registerStatusResource.data}")
                    callbacks?.onRegisterClicked()
                } else {
                    showErrorMessage(
                        (registerStatusResource as Resource.Error<*>).message
                    )
                    Log.d("RegisterFragment", "Resource.Error ${registerStatusResource.message}")
                }
            }

            when (validateState.emailState) {
                ValidateState.EmailState.EMPTY -> {
                    binding.etEmailContainer.error = getString(R.string.email_empty)
                }
                ValidateState.EmailState.INCORRECT -> {
                    binding.etEmailContainer.error = getString(R.string.email_incorrect)
                }
                ValidateState.EmailState.SMALL -> {
                    binding.etEmailContainer.error = getString(R.string.email_small)
                }
            }

            when (validateState.passwordState) {
                ValidateState.PasswordState.EMPTY -> {
                    binding.etPasswordContainer.error = getString(R.string.password_empty)
                }
                ValidateState.PasswordState.SMALL -> {
                    binding.etPasswordContainer.error = getString(R.string.password_small)
                }
            }


        }


    }

    private fun showErrorMessage(message: String?) {

    }

    private fun hideProgressBar() {

    }

    private fun showProgressBar() {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callback?
    }
}