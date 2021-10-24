package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nordside_mobile.model.Category
import com.example.nordside_mobile.repository.NordsideRepository
import com.example.nordside_mobile.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentCategoryViewModel @Inject constructor(
    val repository: NordsideRepository
) : ViewModel()  {

    private var _categoryListLiveData: MutableLiveData<Resource<List<Category>>> = MutableLiveData()
    val categoryListLiveData: LiveData<Resource<List<Category>>> get() = _categoryListLiveData

    init {
        viewModelScope.launch {
            _categoryListLiveData.value = repository.getAllCategory()
        }
    }

}