package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.database.CartPositionPojo
import com.example.nordside_mobile.repository.NordsideRepository

class FragmentCartViewModel: ViewModel() {
    val repository: NordsideRepository = NordsideRepository.get()

    fun getAllCartPosition(): LiveData<List<CartPositionPojo?>> {
        return repository.getAllCartPosition()
    }

    fun saveToCart(code: String, count: Double, summa: Double, title: String, unit: String) {
        repository.saveToCart(code,count,summa,title,unit)
    }

    fun getCartPositionCount(code:String):LiveData<CartPositionPojo?>{
        return repository.getCartPositionsCount(code)
    }

    fun deleteCartPosition(code: String){
        repository.deleteCartPosition(code)
    }

//    fun cleanCart(){
//        repository.cleanCart()
//    }
}