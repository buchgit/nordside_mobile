package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.AppPreference
import com.example.nordside_mobile.model.Nomenclature

import com.example.nordside_mobile.model.NomenclatureCollection

import com.example.nordside_mobile.model.PriceTable

import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.repository.Resource
import com.example.nordside_mobile.usecases.ApplicationConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NomenclatureListViewModel @Inject constructor(
    val repository: NordsideRepository,
    val appPreference: AppPreference
): ViewModel() {

    private var _nomenclatureLiveData: MutableLiveData<Resource<List<Nomenclature>>> = MutableLiveData()
    val nomenclatureListLiveData: LiveData<Resource<List<Nomenclature>>> get() = _nomenclatureLiveData

    private var _nomenclaturePersonalListLiveData: MutableLiveData<Resource<List<PriceTable>>> = MutableLiveData()
    val nomenclaturePersonalListLiveData: LiveData<Resource<List<PriceTable>>> get() = _nomenclaturePersonalListLiveData

    fun getPersonalNomenclatureListByCollection(collectionId: String){

        val token: String = "Bearer ${appPreference.getSavedString(ApplicationConstants().ACCESS_TOKEN)!!}"

        viewModelScope.launch {
            _nomenclaturePersonalListLiveData.value = repository.getPersonalNomenclatureListByCollection(token, collectionId)
        }

    }

}

