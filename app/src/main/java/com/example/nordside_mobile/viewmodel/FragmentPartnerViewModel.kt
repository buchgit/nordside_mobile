package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.model.Partner
import com.example.nordside_mobile.repository.NordsideRepository

class FragmentPartnerViewModel:ViewModel() {
    var partnerList:LiveData<List<Partner>> = NordsideRepository().getAllPartner()

}