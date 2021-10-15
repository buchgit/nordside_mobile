package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.database.SummaCountPojo
import com.example.nordside_mobile.model.Nomenclature
import com.example.nordside_mobile.model.Partner
import com.example.nordside_mobile.repository.NordsideRepository

class NomenclatureItemViewModel:ViewModel() {

    private val repository: NordsideRepository = NordsideRepository.get()
    //var currentNomenclatureCode: String? = repository.getCurrentNomenclature()

    fun saveToCart(code: String, count: Double, summa:Double) {
        repository.saveToCart(code, count, summa)
    }

    fun getCartPositionCount(code:String):LiveData<SummaCountPojo>{
        return repository.getCartPositionsCount(code)
    }

}