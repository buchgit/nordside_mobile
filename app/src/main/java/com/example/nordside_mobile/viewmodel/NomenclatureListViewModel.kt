package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.model.Nomenclature
import com.example.nordside_mobile.repository.NordsideRepository

class NomenclatureListViewModel: ViewModel() {

    fun getNomenclatureByCollection(id:String):LiveData<List<Nomenclature>>{
        return NordsideRepository().getNomenclatureByCollection(id)
    }

}