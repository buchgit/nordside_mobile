package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.model.NomenclatureCollection
import com.example.nordside_mobile.repository.NordsideRepository

class FragmentCollectionViewModel:ViewModel() {
    var  nomenclatureList:LiveData<List<NomenclatureCollection>>? = null
    val repository: NordsideRepository = NordsideRepository.get()

    fun getNomenclatureCollectionByCategoryId(id:String){
        nomenclatureList = repository.getCollectionByCategoryId(id)
    }

}