package com.example.nordside_mobile.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class Order: Serializable {

    lateinit var date: Date
    var summa: Double = 0.0

    @SerializedName("orderLinesTable")
    lateinit var itemTable: List<ClientOrderLine>

    override fun toString(): String {
        return "Order(date=$date, summa=$summa, itemTable=$itemTable)"
    }
}