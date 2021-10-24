package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.model.Nomenclature
import com.example.nordside_mobile.model.NomenclatureCollection
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NomenclatureListViewModel @Inject constructor(
    val repository: NordsideRepository
): ViewModel() {

    private var _nomenclatureLiveData: MutableLiveData<Resource<List<Nomenclature>>> = MutableLiveData()
    val nomenclatureListLiveData: LiveData<Resource<List<Nomenclature>>> get() = _nomenclatureLiveData

    fun getNomenclatureByCollection(id:String){
        viewModelScope.launch {
            _nomenclatureLiveData.value = repository.getNomenclatureByCollection(id)
        }
    }

}




