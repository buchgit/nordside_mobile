package com.example.nordside_mobile.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.repository.NordsideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentCartViewModel @Inject constructor(
    val repository: NordsideRepository
): ViewModel() {

    private val _allCartPosition = MutableSharedFlow<List<CartPositionPojo?>?>()
    val allCartPosition: SharedFlow<List<CartPositionPojo?>?> = _allCartPosition.asSharedFlow()

    init {
        viewModelScope.launch {
            _allCartPosition.emitAll(repository.getAllCartPosition())
        }
    }

    private fun saveToCart(code: String, count: Double, summa: Double, title: String, unit: String, imageUri: Uri?) {
        repository.saveToCart(code, count, summa, title, unit, imageUri)
    }

    fun getCartPositionCount(code: String): LiveData<CartPositionPojo?> {
        return repository.getCartPositionsCount(code)
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

//    fun cleanCart(){
//        repository.cleanCart()
//    }
}