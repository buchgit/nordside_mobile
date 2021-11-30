package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentOrderViewModel @Inject constructor(
    val repository: NordsideRepository
):ViewModel() {

    private var _totalCartSumma: LiveData<Double> = MutableLiveData()
    val totalCartSumma: LiveData<Double> get() = _totalCartSumma

    init {
        //viewModelScope.launch {
            _totalCartSumma = repository.getTotalCartSumma()
        //}
    }

    fun getAllCartPosition(): LiveData<List<CartPositionPojo?>> {
        return repository.getAllCartPosition()
    }

    fun saveToCart(code: String, count: Double, summa: Double, title: String, unit: String) {
        repository.saveToCart(code, count, summa, title, unit)
    }

    fun deleteCartPosition(code: String) {
        repository.deleteCartPosition(code)
    }

}