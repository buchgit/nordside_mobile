package com.nordside_trading.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nordside_trading.model.Nomenclature
import com.nordside_trading.repository.NordsideRepository

class NomenclatureListViewModel: ViewModel() {

    fun getNomenclatureByCollection(id:String):LiveData<List<Nomenclature>>{
        return NordsideRepository().getNomenclatureByCollection(id)
    }

}