package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.withCreated
import com.example.nordside_mobile.R
import com.example.nordside_mobile.databinding.FragmentLoginBinding
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.utils.ApplicationConstants
import com.example.nordside_mobile.viewmodel.FragmentLoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentLogin : Fragment(R.layout.fragment_login) {

    private val TAG = "${FragmentLogin::class.simpleName} ###"

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FragmentLoginViewModel>()
    private var callbacks: Callback? = null
    private lateinit var loginBody: LoginBody

    companion object {
        fun createArgs() = bundleOf(
        )
    }

    interface Callback {
        fun onLoginClicked(isCorrectLogin: Boolean)
        fun onRegistrationClicked(login: LoginBody)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Для метода bind требуется view, можно также через inflate в onCreateView и без конструктора в фрагменте
        _binding = FragmentLoginBinding.bind(view)

        with(binding) {
            buttonLogin.setOnClickListener() { loginButtonListener() }
            buttonRegistration.setOnClickListener { registrationButtonListener() }
        }
    }

    private fun loginButtonListener() {
        loginBody = LoginBody(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        Log.v(TAG, "login ${loginBody.email} ${loginBody.password}")

        viewModel.viewModelScope.launch {
            val isCorrectLogin = viewModel.loginBodyChecker(loginBody)
            val tokenLiveData = withContext(Dispatchers.Default) {
                viewModel.logIn(loginBody)
            }
            // !!!!! Почему-то не работало с оберткой tokenLiveData.value?.let {}
                tokenLiveData.observe(viewLifecycleOwner, Observer {
                    Toast.makeText(requireActivity(), tokenLiveData.value?.token, Toast.LENGTH_SHORT).show()

                    // !!!!! Подумать куда перенести SharedPref.
                    val appSettings = requireActivity().getSharedPreferences(
                        ApplicationConstants().SHARED_PREFERENCES_FILE,
                        Context.MODE_PRIVATE
                    )
                    appSettings.edit()
                        .putString(ApplicationConstants().TOKEN, tokenLiveData.value?.token)
                        .apply()

                })
            callbacks?.onLoginClicked(isCorrectLogin)
        }
    }

    private fun registrationButtonListener() {
        loginBody = LoginBody(binding.etEmail.text.toString(), binding.etPassword.text.toString())
        callbacks?.onRegistrationClicked(loginBody)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callback?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
        _binding = null
    }

}