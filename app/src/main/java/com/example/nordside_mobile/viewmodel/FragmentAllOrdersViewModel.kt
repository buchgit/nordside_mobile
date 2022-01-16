package com.example.nordside_mobile.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.AppPreference
import com.example.nordside_mobile.model.Order
import com.example.nordside_mobile.model.PriceTable
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.repository.Resource
import com.example.nordside_mobile.usecases.ApplicationConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentAllOrdersViewModel @Inject constructor(
    private val repository: NordsideRepository,
    private val appPreference: AppPreference
    ) : ViewModel() {

    private val TAG = "${FragmentAllOrdersViewModel::class.java.simpleName} ###"
    val orderList: MutableLiveData<List<Order>> = MutableLiveData()

    fun saveOrderOnServer(order: Order):String? {
        val token : String? = appPreference.getSavedString(ApplicationConstants().ACCESS_TOKEN)
        var httpStatus: String? = null
        viewModelScope.launch {
            Log.v(TAG,"Bearer $token")
            val result: Resource<String> = repository.saveOrderOnServer("Bearer $token", order)
            if (result is Resource.Success) {
                httpStatus  = result.data
                Log.v(TAG, httpStatus!!)
            }else{
                //TODO сделать навигацию на страничку ошибки "Ваш заказ не сохранен."
                Log.v(TAG,result.message!!)
            }
        }
        return httpStatus
    }

    fun getPersonalOrderList(){
        val token : String? = appPreference.getSavedString(ApplicationConstants().ACCESS_TOKEN)
        var httpStatus: String? = null
        viewModelScope.launch {
            val result:Resource<List<Order>> = repository.getPersonalOrderList("Bearer $token")
            if (result is Resource.Success){
                orderList.value = result.data
            }
        }
    }

}