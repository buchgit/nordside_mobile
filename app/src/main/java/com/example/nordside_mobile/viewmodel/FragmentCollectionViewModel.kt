package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.model.NomenclatureCollection
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentCollectionViewModel @Inject constructor(
    val repository: NordsideRepository
): ViewModel() {

    private var _nomenclatureListLiveData: MutableLiveData<Resource<List<NomenclatureCollection>>> = MutableLiveData()
    val nomenclatureListLiveData: LiveData<Resource<List<NomenclatureCollection>>> get() = _nomenclatureListLiveData

    fun getNomenclatureCollectionByCategoryId(id:String){
        viewModelScope.launch {
            _nomenclatureListLiveData.value = repository.getCollectionByCategoryId(id)
        }
    }

}