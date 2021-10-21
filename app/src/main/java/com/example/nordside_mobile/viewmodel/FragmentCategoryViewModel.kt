package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.model.Category
import com.example.nordside_mobile.repository.NordsideRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentCategoryViewModel @Inject constructor(
    val repository: NordsideRepository
) : ViewModel()  {

    var categoryList:LiveData<List<Category>> = repository.getAllCategory()

}