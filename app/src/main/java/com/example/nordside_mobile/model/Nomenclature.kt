package com.nordside_trading.model

import android.net.Uri
import com.google.gson.annotations.SerializedName
import com.nordside_trading.BuildConfig
import java.io.Serializable

class Nomenclature : Serializable {
    @SerializedName("name")
    var title: String = ""

    //    var code:String  = "",
    @SerializedName("fullName")
    var fullName:String = ""

    var imageIndex:String = ""

//    var description:String = "",
    @SerializedName("length")
    var length: Double = 0.00

    //    var width: Double = 0.00,
//    var size1: Double = 0.00,
//    var size2: Double = 0.00,
//    var diameter: Double = 0.00,
//    var packSquare: Double = 0.00,
//    var color:String = "",
//    var packVolume: Double = 0.00,
//    var packWeight: Double = 0.00,
    @SerializedName("countInPack")
    var countInPackage: Int = 0

    @SerializedName("unit")
    var unit: String = ""

    @SerializedName("high")
    var reliefHeight: Double = 0.00

    val imageUri:Uri
            get(){
                return Uri.parse(BuildConfig.BASE_URL).buildUpon()
                    .appendPath("resources")
                    .appendPath("static")
                    .appendPath("images")
                    .appendPath("${imageIndex}.JPEG")
                    .build()
            }
}

/* TODO убрать из JSON категорию и коллекцию, если не понадобится для чего-нибудь
{
        "id": 100016,
        "name": "панель 05*250*2700 белый матовый",
        "code": "00000000318",
        "fullName": "панель 05*250*2700 белый матовый",
        "imageIndex": "00000000318",
        "description": "без описания",
        "category": {
            "id": 100006,
            "name": "ПВХ панели"
        },
        "nomenclatureCollection": {
            "id": 100008,
            "name": "Панели с офсетной печатью"
        },
        "length": 2700,
        "width": 250,
        "size1": 0,
        "size2": 0,
        "diameter": 0,
        "high": 5,
        "packSquare": 5.005,
        "color": "белый",
        "packVolume": 0.0034,
        "packWeight": 4.03,
        "countInPack": 10,
        "unit": "шт"
    }
 */