package com.example.nordside_mobile.model

import com.google.gson.annotations.SerializedName
import java.util.*

class Order(
    var date: Date,
    var summa: Double,
    @SerializedName("orderLinesTable")
    var itemTable: List<ClientOrderLine>
)