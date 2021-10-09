package com.example.nordside_mobile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.nordside_mobile.model.Category
import com.example.nordside_mobile.repository.NordsideRepository

class FragmentCategoryViewModel: ViewModel() {

    var categoryList:LiveData<List<Category>> = NordsideRepository().getAllCategory()

}