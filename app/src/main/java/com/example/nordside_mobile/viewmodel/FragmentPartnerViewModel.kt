package com.nordside_trading.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nordside_trading.model.Partner
import com.nordside_trading.repository.NordsideRepository

class FragmentPartnerViewModel:ViewModel() {
    var partnerList:LiveData<List<Partner>> = NordsideRepository().getAllPartner()

}