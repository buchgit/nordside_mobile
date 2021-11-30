package com.example.nordside_mobile.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

class Order(
    var id:String?,
    var date: LocalDate,
    var number_1c:String?,
    var summa:Double,
    @SerializedName("orderLinesTable")
    var itemTable:List<ClientOrderLine>
    ) {
}