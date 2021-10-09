package com.nordside_trading.model

import com.nordside_trading.model.PriceTable

class PriceTableArray {
    lateinit var listOfPriceLines:List<PriceTable>
}

/*
[ //массив
    {//PriceTable::class - строка с номенклатурой, единицей измерения, ценой
        "id": 100004,
        "nomenclature": { //Nomenclature::class
            "id": 100003,
            "name": "панель 05*250*2700 белый матовый",
            "code": "00000009457",
            "fullName": "панель 05*250*2700 белый матовый",
            "imageIndex": "00000009457",
            "description": "без описания",
            "section": "Панели ПВХ",
            "subsection": "Панели ПВХ стеновые \"Матовые\"",
            "length": 2700.0,
            "width": 250.0,
            "size1": 0.0,
            "size2": 0.0,
            "diameter": 0.0,
            "high": 5.0,
            "packSquare": 5.005,
            "color": "белый",
            "packVolume": 0.0034,
            "packWeight": 4.03,
            "countInPack": 10,
            "unit": "шт"
        },
        "unit": "шт",
        "price": 1.12
    }
]
 */