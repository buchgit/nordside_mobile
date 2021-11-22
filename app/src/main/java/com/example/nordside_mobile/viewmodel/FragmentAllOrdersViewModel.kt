package com.example.nordside_mobile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.model.Order
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentAllOrdersViewModel @Inject constructor(
    private val repository: NordsideRepository
    ) : ViewModel() {

    private val TAG = "${FragmentAllOrdersViewModel::class.java.simpleName} ###"

    fun saveOrderOnServer(order: Order):String? {
        var httpStatus: String? = null
        viewModelScope.launch {
            val result: Resource<String> = repository.saveOrderOnServer(order)
            if (result is Resource.Success) {
                httpStatus  = result.data
            }else{
                //TODO сделать навигацию на страничку ошибки "Ваш заказ не сохранен."
                Log.v(TAG,result.message!!)
            }
        }
        return httpStatus
    }

}