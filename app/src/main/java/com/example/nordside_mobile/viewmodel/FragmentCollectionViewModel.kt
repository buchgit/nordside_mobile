package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.model.NomenclatureCollection
import com.example.nordside_mobile.repository.NordsideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentCollectionViewModel @Inject constructor(
    val repository: NordsideRepository
): ViewModel() {

    var  nomenclatureList:LiveData<List<NomenclatureCollection>>? = null

    fun getNomenclatureCollectionByCategoryId(id:String){
        nomenclatureList = repository.getCollectionByCategoryId(id)
    }

}