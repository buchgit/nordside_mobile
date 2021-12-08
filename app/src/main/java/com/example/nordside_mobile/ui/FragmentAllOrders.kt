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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.nordside_mobile.R
import com.example.nordside_mobile.model.Order
import com.example.nordside_mobile.model.ClientOrderLine
import com.example.nordside_mobile.viewmodel.FragmentAllOrdersViewModel
import com.example.nordside_mobile.viewmodel.FragmentCartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.time.LocalDate

@AndroidEntryPoint
class FragmentAllOrders:Fragment(R.layout.fragment_all_orders) {
    private val allOrdersViewModel by viewModels<FragmentAllOrdersViewModel>()
    private val cartViewModel by viewModels<FragmentCartViewModel>()
    private lateinit var recyclerView: RecyclerView
    private var callback: BottomNavigationButtonCallback? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callback!!.setButtonVisible(R.string.buy, false)

        makeAnOrder()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun makeAnOrder() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            cartViewModel.allCartPosition.collect {
                val summaOfOrder = 0.00
                val orderLineList = mutableListOf<ClientOrderLine>()

                it?.forEach { cartLine ->
                    cartLine?.apply {
                        orderLineList.add(ClientOrderLine(code, title, unit, count, summa))
                        summaOfOrder.plus(summa ?: 0.00)
                    }
                }

                val order = Order("", LocalDate.now(),"", summaOfOrder, orderLineList)
                allOrdersViewModel.saveOrderOnServer(order)
            }
        }
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