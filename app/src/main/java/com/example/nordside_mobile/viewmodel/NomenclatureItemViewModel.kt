package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.repository.NordsideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NomenclatureItemViewModel @Inject constructor(
    private val repository: NordsideRepository
):ViewModel() {

    private val TAG =  "${NomenclatureItemViewModel::class.java.simpleName} ###"

    fun saveToCart(owner: LifecycleOwner, code: String, count: Double, summa:Double, title:String, unit:String) {
        repository.saveToCart(code, count, summa, title, unit)
    }

    fun getCartPositionCount(code:String):LiveData<CartPositionPojo?>{
        return repository.getCartPositionsCount(code)
    }

    fun deleteCartPosition(code: String) {
        repository.deleteCartPosition(code)
    }

}