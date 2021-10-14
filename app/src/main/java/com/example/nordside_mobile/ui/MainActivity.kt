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

    lateinit var appSettins: SharedPreferences

    private lateinit var binding: ActivityMainBinding

    lateinit var navController:NavController

    val repository: NordsideRepository = NordsideRepository.get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.also {
            it.setDisplayShowTitleEnabled(false)
        }

        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        //инициализируем файл для сохраниния параметров приложения
        appSettins = getSharedPreferences(
            ApplicationConstants().SHARED_PREFERENCES_FILE,
            Context.MODE_PRIVATE
        )
    }

//    //back button on action var
//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }

    //проброска клика по категории во фрагмент
    override fun onCategorySelected(category: Category?) {
        if (category != null) {
            Log.v(TAG, category.title)
            val bundle = Bundle()
            bundle.putString("id", category.id)
            bundle.putString("category_title",category.title)
            navController.navigate(R.id.action_fragmentCommon_to_fragmentCollection, bundle)
        }
    }

    //проброска клика по коллекции во фрагмент
    override fun onCollectionSelected(nomenclatureCollection: NomenclatureCollection) {
        Log.v(TAG, nomenclatureCollection.id)
        val bundle = Bundle()
        bundle.putString("id", nomenclatureCollection.id)
        bundle.putString("collection_title", nomenclatureCollection.title)
        navController.navigate(R.id.action_fragmentCollection_to_fragmentNomenclatureList, bundle)
    }

    //проброска клика по позиции номенклатуры в списке, открывает карточку номенклатуры на всю страничку
    override fun onNomenclatureSelected(nomenclature: Nomenclature) {
        Log.v(TAG, nomenclature.toString())
        val bundle = Bundle()
        bundle.putSerializable("nomenclature", nomenclature)
        bundle.putString("nomenclature_name",nomenclature.title)
        navController.navigate(R.id.action_fragmentNomenclatureList_to_fragmentNomenclatureItem, bundle)
    }

    override fun onLoginClicked(login: LoginBody) {
        val token: LiveData<ServerToken> = repository.login(login)
        token.observe(this,
            Observer {
                Toast.makeText(this, token.value?.token, Toast.LENGTH_SHORT).show()
                appSettins.edit().putString(ApplicationConstants().TOKEN, token.value?.token)
                    .apply()
            })

    }

    override fun onRegistrationClicked(login: LoginBody) {

    }

}