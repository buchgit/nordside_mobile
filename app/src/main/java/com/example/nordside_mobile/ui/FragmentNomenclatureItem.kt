package com.example.nordside_mobile.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.example.nordside_mobile.R
import com.example.nordside_mobile.databinding.FragmentNomenclatureItemBinding
import com.example.nordside_mobile.model.Nomenclature
import com.example.nordside_mobile.viewmodel.NomenclatureItemViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class FragmentNomenclatureItem : Fragment(R.layout.fragment_nomenclature_item) {

    private var _binding: FragmentNomenclatureItemBinding? = null
    private val binding get() = _binding!!

    private val TAG =  "${FragmentNomenclatureItem::class.simpleName} ###"
    private lateinit var currentNomenclature: Nomenclature

    private val nomenclatureItemViewModel: NomenclatureItemViewModel by viewModels()
    private var COUNT_TO_CART = 1.00
    private var COUNT_TO_CART_MINUS = -1.00
    private val DEFAULT_PRICE = 11.01

    companion object {
        fun createArgs(nomenclature: Nomenclature) = bundleOf(
            "nomenclature" to nomenclature,
            "nomenclature_name" to nomenclature.title
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentNomenclature = arguments?.getSerializable("nomenclature") as Nomenclature
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNomenclatureItemBinding.bind(view)

        with(binding) {

            twFragmentNomenclatureItem1.text = currentNomenclature.fullName
            twFragmentNomenclatureItem2.text = currentNomenclature.unit

            nomenclatureItemViewModel.getCartPositionCount(currentNomenclature.code)
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    //count in the cart
                    twFragmentNomenclatureItem3.text = it.count.toString()
                })

            buttonAddFragmentNomenclatureItem.setOnClickListener(View.OnClickListener {
                Log.v(TAG,it.id.toString())
                nomenclatureItemViewModel.saveToCart(currentNomenclature.code, COUNT_TO_CART, DEFAULT_PRICE)
            })

            buttonDeleteFragmentNomenclatureItem.setOnClickListener(View.OnClickListener {
                nomenclatureItemViewModel.saveToCart(currentNomenclature.code, COUNT_TO_CART_MINUS, DEFAULT_PRICE)
            })

        }

        //webView.loadUrl("http://192.168.1.179:8080/ru.nordside-1.4-SNAPSHOT/resources/static/images/00000000318.JPEG")

        Glide.with(this)
            .load(currentNomenclature.imageUri)
            .into(binding.ivFragmentNomenclatureItem1)
    }

    override fun onDetach() {
        super.onDetach()
        _binding = null
    }
}