package com.example.nordside_mobile.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
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

    private var currentFragment: Fragment? = null

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is NavHostFragment) return
            currentFragment = f
            updateUi()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.also { it.setDisplayShowTitleEnabled(false) }

        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)
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

    fun goBack() {
        onBackPressed()
    }

    private fun updateUi() {
        when(currentFragment) {
            is FragmentNomenclatureList, is FragmentNomenclatureItem, is FragmentCollection -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                }
            else -> {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                supportActionBar?.setDisplayShowHomeEnabled(false)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
       return navController.navigateUp() || super.onSupportNavigateUp()
   }
}