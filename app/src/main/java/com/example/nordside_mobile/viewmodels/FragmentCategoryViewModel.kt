package com.example.nordside_mobile.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.model.Category
import com.example.nordside_mobile.data.repositories.NordsideRepository

class FragmentCategoryViewModel: ViewModel() {

    val repository: NordsideRepository = NordsideRepository.get()

    var categoryList:LiveData<List<Category>> = repository.getAllCategory()

}