package com.example.nordside_mobile.database

import android.net.Uri

data class CartPositionPojo (
    var code :String?,
    var title:String?,
    var unit:String?,
    var count:Double?,
    var summa:Double?,
    var imageUri: Uri?
)