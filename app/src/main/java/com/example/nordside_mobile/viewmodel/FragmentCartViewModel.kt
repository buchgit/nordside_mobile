package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.repository.NordsideRepository

class FragmentCartViewModel: ViewModel() {
    val repository: NordsideRepository = NordsideRepository.get()
}