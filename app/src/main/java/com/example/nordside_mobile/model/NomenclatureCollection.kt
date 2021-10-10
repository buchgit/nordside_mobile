package com.example.nordside_mobile.model

import com.google.gson.annotations.SerializedName

data class NomenclatureCollection(
    var id: String = "",
    @SerializedName("name") var title: String = ""
)