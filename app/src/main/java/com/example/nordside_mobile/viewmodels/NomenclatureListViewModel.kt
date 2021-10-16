package com.example.nordside_mobile.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.model.Nomenclature
import com.example.nordside_mobile.data.repositories.NordsideRepository

class NomenclatureListViewModel: ViewModel() {

    val repository: NordsideRepository = NordsideRepository.get()

    fun getNomenclatureByCollection(id:String):LiveData<List<Nomenclature>>{
        return repository.getNomenclatureByCollection(id)
    }

}