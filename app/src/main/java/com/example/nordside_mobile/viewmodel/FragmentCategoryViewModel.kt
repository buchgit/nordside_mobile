package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.model.Category
import com.example.nordside_mobile.repository.NordsideRepository

class FragmentCategoryViewModel: ViewModel() {

    val repository: NordsideRepository = NordsideRepository.get()

    var categoryList:LiveData<List<Category>> = repository.getAllCategory()

}