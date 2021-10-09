package com.nordside_trading.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nordside_trading.model.NomenclatureCollection
import com.nordside_trading.repository.NordsideRepository

class FragmentCollectionViewModel:ViewModel() {
    var  nomenclatureList:LiveData<List<NomenclatureCollection>>? = null

    fun getNomenclatureCollectionByCategoryId(id:String){
        nomenclatureList = NordsideRepository().getCollectionByCategoryId(id)
    }

}