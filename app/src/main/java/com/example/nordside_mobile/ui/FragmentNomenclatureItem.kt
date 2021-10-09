package com.nordside_trading.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.nordside_trading.R
import com.nordside_trading.model.Nomenclature


class FragmentNomenclatureItem : Fragment() {

    private lateinit var currentNomenclature: Nomenclature
    private lateinit var webView: WebView
    private lateinit var textView_1: TextView
    private lateinit var textView_2: TextView

    companion object {
        fun newInstance(nomenclature: Nomenclature): FragmentNomenclatureItem {
            return FragmentNomenclatureItem().apply {
                arguments = Bundle().apply { putSerializable("NOMENCLATURE", nomenclature) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentNomenclature = arguments?.getSerializable("NOMENCLATURE") as Nomenclature
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

        textView_1.setText(currentNomenclature.fullName)
        textView_2.setText(currentNomenclature.unit)

        webView = view.findViewById(R.id.wc_fragment_nomenclature_item_1)
        webView.settings.setJavaScriptEnabled(true)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        val uri: Uri = currentNomenclature.imageUri

        //webView.loadUrl("http://192.168.1.179:8080/ru.nordside-1.4-SNAPSHOT/resources/static/images/00000000318.JPEG")
        webView.loadUrl(uri.toString())
        return view
    }

}