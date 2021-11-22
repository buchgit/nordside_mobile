package com.example.nordside_mobile.model

class Order(
    var id:String?,
    var number_1c:String?,
    var summa:Double,
    var itemTable:List<OrderLine>
    ) {
}