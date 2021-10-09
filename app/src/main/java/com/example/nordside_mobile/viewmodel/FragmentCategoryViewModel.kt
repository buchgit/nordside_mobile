package com.nordside_trading.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.nordside_trading.model.Category
import com.nordside_trading.repository.NordsideRepository

class FragmentCategoryViewModel: ViewModel() {

    var categoryList:LiveData<List<Category>> = NordsideRepository().getAllCategory()

}