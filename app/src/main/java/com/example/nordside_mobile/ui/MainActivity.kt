package com.example.nordside_mobile.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nordside_mobile.utils.ApplicationConstants
import com.example.nordside_mobile.R
import com.example.nordside_mobile.databinding.ActivityMainBinding
import com.example.nordside_mobile.model.*
import com.example.nordside_mobile.repository.NordsideRepository
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), FragmentCategory.Callback, FragmentCollection.Callback,
    FragmentNomenclatureList.CallbackNomenclature, FragmentLogin.Callback {

    private var TAG = "${MainActivity::class.simpleName} ###"
    private lateinit var binding: ActivityMainBinding
    lateinit var navController:NavController
    val repository: NordsideRepository = NordsideRepository.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.also { it.setDisplayShowTitleEnabled(false) }

        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)


    }

    // Общая функций для навигации
    private fun launchDestination(destinationId: Int, args: Bundle? = null) {
        if (args != null) {
            navController.navigate(destinationId, args)
        } else {
            navController.navigate(destinationId)
        }
    }

    // Проброска клика по категории во фрагмент
    override fun onCategorySelected(category: Category?) {
        category?.let {
            Log.v(TAG, category.title)
            launchDestination(R.id.fragmentCollection, FragmentCollection.createArgs(category))
        }
    }

    // Проброска клика по коллекции во фрагмент
    override fun onCollectionSelected(nomenclatureCollection: NomenclatureCollection) {
        Log.v(TAG, nomenclatureCollection.id)
        launchDestination(
            R.id.fragmentNomenclatureList,
            FragmentNomenclatureList.createArgs(nomenclatureCollection)
        )
    }

    // Проброска клика по позиции номенклатуры в списке, открывает карточку номенклатуры на всю страничку
    override fun onNomenclatureSelected(nomenclature: Nomenclature) {
        Log.v(TAG, nomenclature.toString())
        launchDestination(
            R.id.fragmentNomenclatureItem,
            FragmentNomenclatureItem.createArgs(nomenclature)
        )
    }

    override fun onLoginClicked(isCorrectLogin: Boolean) {
        if (isCorrectLogin) {
            launchDestination(R.id.fragmentPersonal)
        } else {
            launchDestination(R.id.fragmentForgotPass)
        }
    }

    override fun onRegistrationClicked(login: LoginBody) {

    }


//    //back button on action var
//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
}