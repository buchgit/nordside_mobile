package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.nordside_mobile.R
import com.example.nordside_mobile.databinding.FragmentPersonalOfficeBinding

class FragmentPersonal : Fragment(R.layout.fragment_personal_office)  {

    private var _binding: FragmentPersonalOfficeBinding? = null
    private val binding get() = _binding!!
    private var callback: Callback? = null

    interface Callback{
        fun onMyOrdersClicked()
        fun onChangePasswordClicked()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPersonalOfficeBinding.bind(view)

        with(binding){
            buttonFragmentPersonalMyOrders.setOnClickListener{myOrdersClickListener()}
            buttonFragmentPersonalChangePassword.setOnClickListener{changePasswordClickListener()}
        }

    }

    private fun changePasswordClickListener() {
        callback!!.onChangePasswordClicked()
    }

    private fun myOrdersClickListener() {
        callback!!.onMyOrdersClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as Callback?
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
        _binding = null
    }

}