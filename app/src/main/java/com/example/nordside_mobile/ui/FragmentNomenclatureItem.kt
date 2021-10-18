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
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import com.example.nordside_mobile.R
import com.example.nordside_mobile.model.Nomenclature
import com.example.nordside_mobile.viewmodel.NomenclatureItemViewModel
import com.google.android.material.button.MaterialButton
import java.util.*

class FragmentNomenclatureItem : Fragment() {

    private val TAG =  "${FragmentNomenclatureItem::class.simpleName} ###"
    private lateinit var currentNomenclature: Nomenclature
    private lateinit var webView: WebView
    private lateinit var textView_1: TextView
    private lateinit var textView_2: TextView
    private lateinit var textView_3: TextView
    private lateinit var button_add:MaterialButton
    private lateinit var button_del:MaterialButton
    private val nomenclatureItemViewModel by viewModels<NomenclatureItemViewModel>()
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

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nomenclature_item, container, false)
        textView_1 = view.findViewById(R.id.tw_fragment_nomenclature_item_1)
        textView_2 = view.findViewById(R.id.tw_fragment_nomenclature_item_2)
        textView_3 = view.findViewById(R.id.tw_fragment_nomenclature_item_3)
        button_add = view.findViewById(R.id.button_add_fragment_nomenclature_item)
        button_del = view.findViewById(R.id.button_delete_fragment_nomenclature_item)

        textView_1.setText(currentNomenclature.fullName)
        textView_2.setText(currentNomenclature.unit)

        nomenclatureItemViewModel.getCartPositionCount(currentNomenclature.code)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                //count in the cart
                textView_3.setText(it.count.toString())
            })

        webView = view.findViewById(R.id.wc_fragment_nomenclature_item_1)
        webView.settings.setJavaScriptEnabled(true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        button_add.setOnClickListener(View.OnClickListener {
            Log.v(TAG,it.id.toString())
            nomenclatureItemViewModel.saveToCart(currentNomenclature.code, COUNT_TO_CART, DEFAULT_PRICE)
        })

        button_del.setOnClickListener(View.OnClickListener {
            nomenclatureItemViewModel.saveToCart(currentNomenclature.code, COUNT_TO_CART_MINUS, DEFAULT_PRICE)
        })

        val uri: Uri = currentNomenclature.imageUri

        //webView.loadUrl("http://192.168.1.179:8080/ru.nordside-1.4-SNAPSHOT/resources/static/images/00000000318.JPEG")
        webView.loadUrl(uri.toString())
        return view
    }

}