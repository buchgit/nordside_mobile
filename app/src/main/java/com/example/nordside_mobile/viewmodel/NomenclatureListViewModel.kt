package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.model.Nomenclature
import com.example.nordside_mobile.repository.NordsideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NomenclatureListViewModel @Inject constructor(
    val repository: NordsideRepository
): ViewModel() {

    fun getNomenclatureByCollection(id:String):LiveData<List<Nomenclature>>{
        return repository.getNomenclatureByCollection(id)
    }

}