package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.repository.NordsideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentOrderViewModel @Inject constructor(
    val repository: NordsideRepository
):ViewModel() {

}