package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.model.Category
import com.example.nordside_mobile.model.Partner
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentPartnerViewModel @Inject constructor(
    val repository: NordsideRepository
):ViewModel() {

    private var _partnerListLiveData: MutableLiveData<Resource<List<Partner>>> = MutableLiveData()
    val partnerListLiveData: LiveData<Resource<List<Partner>>> get() = _partnerListLiveData

    init {
        viewModelScope.launch {
            _partnerListLiveData.value = repository.getAllPartner()
        }
    }

}