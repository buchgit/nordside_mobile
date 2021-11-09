package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.nordside_mobile.R
import com.example.nordside_mobile.viewmodel.FragmentAllOrdersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAllOrders:Fragment() {
    private val viewModel by viewModels<FragmentAllOrdersViewModel>()
    private lateinit var recyclerView: RecyclerView
    private var callback: BottomNavigationButtonCallback? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_all_orders, container, false)
        val textView_title = view.findViewById(R.id.tw_fragment_all_orders_1) as TextView
        //recyclerView =
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callback!!.setButtonVisible(R.string.buy, false)

    }

//    override fun onStop() {
//        super.onStop()
//        callback!!.setButtonVisible(R.string.cart, true)
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as BottomNavigationButtonCallback
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

}