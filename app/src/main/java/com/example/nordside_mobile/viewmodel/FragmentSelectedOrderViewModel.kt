package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.model.ClientOrderLine
import com.example.nordside_mobile.model.Order
import java.util.*

class FragmentSelectedOrderViewModel () : ViewModel() {

    public var order: Order? = null

    fun getOrderDate(): Date {
        return order!!.date
    }

    fun getOrderSumma():Double{
        return order!!.summa
    }

    fun getOrderItemTable(): List<ClientOrderLine>{
        return order!!.itemTable
    }

}