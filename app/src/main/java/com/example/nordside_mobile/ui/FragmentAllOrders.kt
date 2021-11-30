package com.example.nordside_mobile.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.nordside_mobile.R
import com.example.nordside_mobile.model.Order
import com.example.nordside_mobile.model.ClientOrderLine
import com.example.nordside_mobile.viewmodel.FragmentAllOrdersViewModel
import com.example.nordside_mobile.viewmodel.FragmentCartViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class FragmentAllOrders:Fragment() {
    private val allOrdersViewModel by viewModels<FragmentAllOrdersViewModel>()
    private val cartViewModel by viewModels<FragmentCartViewModel>()
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

        makeAnOrder()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun makeAnOrder() {
        //get cart items
        cartViewModel.getAllCartPosition().observe(viewLifecycleOwner,
        Observer {
            val summa:Double = 0.00
            val orderLineList = mutableListOf<ClientOrderLine>()
            for (i in it.indices){
                val cartLine = it[i]
                val orderLine  = ClientOrderLine(
                    cartLine?.code,
                    cartLine?.title,
                    cartLine?.unit,
                    cartLine?.count,
                    cartLine?.summa
                )
                orderLineList.add(orderLine)
                summa.plus(cartLine?.summa!!)
            }
            val order = Order("", LocalDate.now(),"", summa, orderLineList)
            allOrdersViewModel.saveOrderOnServer(order)
        })
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