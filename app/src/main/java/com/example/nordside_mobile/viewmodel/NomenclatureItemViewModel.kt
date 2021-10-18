package com.example.nordside_mobile.viewmodel

import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.ui.FragmentNomenclatureItem

class NomenclatureItemViewModel:ViewModel() {

    private val TAG =  "${NomenclatureItemViewModel::class.simpleName} ###"
    private val repository: NordsideRepository = NordsideRepository.get()

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