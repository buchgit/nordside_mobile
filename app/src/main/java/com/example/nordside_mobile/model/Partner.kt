package com.nordside_trading.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Partner :Serializable{
    @SerializedName("name")
    var title: String = ""
    var telephone: String = ""
    var site: String = ""
    var address: String = ""
}
