package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.model.Partner
import com.example.nordside_mobile.repository.NordsideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentPartnerViewModel @Inject constructor(
    val repository: NordsideRepository
):ViewModel() {

    var partnerList:LiveData<List<Partner>> = repository.getAllPartner()

}