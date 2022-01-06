package com.example.nordside_mobile.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentOrderViewModel @Inject constructor(
    val repository: NordsideRepository
):ViewModel() {

    private val _totalCartSumma = MutableSharedFlow<Double?>()
    val totalCartSumma: SharedFlow<Double?> = _totalCartSumma.asSharedFlow()

    private val _allCartPosition = MutableSharedFlow<List<CartPositionPojo?>?>()
    val allCartPosition: SharedFlow<List<CartPositionPojo?>?> = _allCartPosition.asSharedFlow()

    init {
        viewModelScope.launch {
            _totalCartSumma.emitAll(repository.getTotalCartSumma())
        }
        viewModelScope.launch {
            _allCartPosition.emitAll(repository.getAllCartPosition())
        }
    }

    private fun saveToCart(code: String, count: Double, summa: Double, title: String, unit: String, imageUri: Uri?) {
        repository.saveToCart(code, count, summa, title, unit, imageUri)
    }

    private fun deleteCartPosition(code: String) {
        repository.deleteCartPosition(code)
    }

    fun changeCart(
        currentCartPosition: CartPositionPojo?,
        currentCount: Double?,
        currentSumma: Double?,
    ) {
        if (currentCount!! <= 0.00) {
            deleteCartPosition(currentCartPosition!!.code!!)
            return
        }
        saveToCart(
            currentCartPosition?.code!!,
            currentCount,
            currentSumma!!,
            currentCartPosition.title!!,
            currentCartPosition.unit!!,
            currentCartPosition.imageUri
        )
    }

}