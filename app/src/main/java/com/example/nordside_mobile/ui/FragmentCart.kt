package com.example.nordside_mobile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nordside_mobile.databinding.FragmentCartBinding
import com.example.nordside_mobile.viewmodel.FragmentCartViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCart:Fragment() {

    private val cartViewModel by viewModels<FragmentCartViewModel>()
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView = binding.twFragmentCart
        val recyclerView = binding.recyclerViewFragmentCart

        //TODO загрузку списка номенклатуры из корзины через cartViewModel


        return root
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}