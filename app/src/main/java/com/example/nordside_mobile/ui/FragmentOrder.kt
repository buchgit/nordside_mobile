package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nordside_mobile.R
import com.example.nordside_mobile.viewmodel.FragmentOrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentOrder:Fragment() {
    private val orderViewModel by viewModels<FragmentOrderViewModel>()
    private var callback: BottomNavigationButtonCallback? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_order, container, false)
        val textView_title = view.findViewById(R.id.tw_fragment_order_1) as TextView
        val textView_items = view.findViewById(R.id.tw_fragment_order_2) as TextView
        val textView_summa = view.findViewById(R.id.tw_fragment_order_3) as TextView
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callback!!.setButtonVisible(R.string.buy, true)

    }

    override fun onStop() {
        super.onStop()
        callback!!.setButtonVisible(R.string.buy, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as BottomNavigationButtonCallback
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

}