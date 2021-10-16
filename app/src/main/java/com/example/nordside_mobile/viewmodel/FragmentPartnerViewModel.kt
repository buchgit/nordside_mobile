package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.model.Partner
import com.example.nordside_mobile.repository.NordsideRepository

class FragmentPartnerViewModel:ViewModel() {

    val repository: NordsideRepository = NordsideRepository.get()
    var partnerList:LiveData<List<Partner>> = repository.getAllPartner()

}