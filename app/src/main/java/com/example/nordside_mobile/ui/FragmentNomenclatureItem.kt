package com.example.nordside_mobile.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
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
import androidx.lifecycle.Observer
import com.example.nordside_mobile.R
import com.example.nordside_mobile.databinding.FragmentNomenclatureItemBinding
import com.example.nordside_mobile.model.Nomenclature
import com.example.nordside_mobile.model.PriceTable
import com.example.nordside_mobile.viewmodel.NomenclatureItemViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class FragmentNomenclatureItem : Fragment(R.layout.fragment_nomenclature_item) {

    private var _binding: FragmentNomenclatureItemBinding? = null
    private val binding get() = _binding!!

    private val TAG =  "${FragmentNomenclatureItem::class.simpleName} ###"
    private lateinit var currentNomenclatureWithPrice: PriceTable
    private lateinit var currentNomenclature: Nomenclature

    private val nomenclatureItemViewModel: NomenclatureItemViewModel by viewModels()
    private var COUNT_TO_CART = 1.00
    private lateinit var webView: WebView
    private lateinit var textView_1: TextView
    private lateinit var textView_2: TextView
    private lateinit var textView_3: TextView
    private lateinit var button_add:MaterialButton
    private lateinit var button_del:MaterialButton
    private val nomenclatureItemViewModel by viewModels<NomenclatureItemViewModel>()
    private var COUNT_TO_CART_PLUS = 1.00
    private var COUNT_TO_CART_MINUS = -1.00
    private var CURRENT_SUMMA = 0.00
    private var CURRENT_PRICE = 0.00
    private var currentCount:Double? = null
    private var currentSumma:Double? = null
    private var currentPrice:Double? = null

    companion object {
        fun createArgs(nomenclatureWithPrice: PriceTable) = bundleOf(
            "nomenclatureWithPrice" to nomenclatureWithPrice,
            "nomenclature_name" to (nomenclatureWithPrice.nomenclature?.title ?: "")
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentNomenclature = arguments?.getSerializable("nomenclature") as Nomenclature
    }
        currentNomenclatureWithPrice = arguments?.getSerializable("nomenclatureWithPrice") as PriceTable


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNomenclatureItemBinding.bind(view)

        with(binding) {
    @SuppressLint("SetJavaScriptEnabled", "SetTextI18n", "ResourceType")
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

            twFragmentNomenclatureItem1.text = currentNomenclature.fullName
            twFragmentNomenclatureItem2.text = currentNomenclature.unit
        textView_1.setText(currentNomenclature.fullName ?: "")
        textView_2.setText(currentNomenclature.unit ?: "")

            nomenclatureItemViewModel.getCartPositionCount(currentNomenclature.code)
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    //count in the cart
                    twFragmentNomenclatureItem3.text = it.count.toString()
                })
        currentNomenclature.let {
            nomenclatureItemViewModel.getCartPositionCount(it.code)
                .observe(viewLifecycleOwner,
                    Observer {
                        if (it != null) {
                            if (it.count != null) {
                                currentCount = it.count
                                currentSumma = it.summa
                                textView_3.setText(
                                    String.format("%.2f", currentCount)) //two digit after decimal point
                            } else {
                                currentCount = 0.00
                                currentSumma = 0.00
                                textView_3.setText(getString(R.string.zero))
                            }
                        } else {
                            currentCount = 0.00
                            currentSumma = 0.00
                            textView_3.setText(getString(R.string.zero))
                        }
                    }
                )
        }

            buttonAddFragmentNomenclatureItem.setOnClickListener(View.OnClickListener {
                Log.v(TAG,it.id.toString())
                nomenclatureItemViewModel.saveToCart(currentNomenclature.code, COUNT_TO_CART, DEFAULT_PRICE)
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
            changeCart(this.button_add)
        })

            buttonDeleteFragmentNomenclatureItem.setOnClickListener(View.OnClickListener {
                nomenclatureItemViewModel.saveToCart(currentNomenclature.code, COUNT_TO_CART_MINUS, DEFAULT_PRICE)
            })
        button_del.setOnClickListener(View.OnClickListener {
            changeCart(this.button_del)
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
    fun changeCart(event: View) {
        if (currentCount == null || currentSumma == null || currentCount!!.compareTo(0.00) == 0) {
            currentPrice = 0.00
        } else {
            currentPrice = currentNomenclatureWithPrice.price
            CURRENT_SUMMA = currentSumma as Double
            if (event.id == R.id.button_add_fragment_nomenclature_item) {
                CURRENT_SUMMA += currentPrice!!
                COUNT_TO_CART_PLUS = currentCount?.plus(1.00) ?: 1.00
                nomenclatureItemViewModel.saveToCart(
                    viewLifecycleOwner,
                    currentNomenclature.code,
                    COUNT_TO_CART_PLUS,
                    CURRENT_SUMMA,
                    currentNomenclature.title,
                    currentNomenclature.unit
                )
            } else if (event.id == R.id.button_delete_fragment_nomenclature_item) {
                currentPrice = currentSumma!! / currentCount!!
                CURRENT_SUMMA -= currentPrice!!
                if (currentCount == null) {
                    return
                }
                COUNT_TO_CART_MINUS = currentCount?.minus(1.00) ?: 0.00
                if (COUNT_TO_CART_MINUS.compareTo(0.00) <= 0) {
                    nomenclatureItemViewModel.deleteCartPosition(currentNomenclature.code)
                } else {
                    nomenclatureItemViewModel.saveToCart(
                        viewLifecycleOwner,
                        currentNomenclature.code,
                        COUNT_TO_CART_MINUS,
                        -CURRENT_SUMMA,
                        currentNomenclature.title,
                        currentNomenclature.unit
                    )
                }
            }
        }
    }

}