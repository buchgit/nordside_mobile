package com.example.nordside_mobile.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nordside_mobile.ApplicationConstants
import com.example.nordside_mobile.R
import com.example.nordside_mobile.databinding.ActivityMainBinding
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.Nomenclature
import com.example.nordside_mobile.model.ServerToken
import com.example.nordside_mobile.repository.NordsideRepository
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), FragmentCategory.Callback, FragmentCollection.Callback,
    FragmentNomenclatureList.CallbackNomenclature, FragmentLogin.Callback {

    private var TAG = "${MainActivity::class.simpleName} ###"

    lateinit var appSettins: SharedPreferences

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_login, R.id.navigation_cart
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //инициализируем файл для сохраниния параметров приложения
        appSettins = getSharedPreferences(
            ApplicationConstants().SHARED_PREFERENCES_FILE,
            Context.MODE_PRIVATE
        )

    }

    //проброска клика по категории во фрагмент
    override fun onCategorySelected(id: String) {
        Log.v(TAG, id)
        val collectionFragment = supportFragmentManager.findFragmentById(R.id.container_fragment_2) as FragmentCollection
        collectionFragment.onCategorySelected(id)
    }

    //    //проброска клика по коллекции во фрагмент
    override fun onCollectionSelected(id: String) {
//        //Log.v(TAG, id)
//        val fragment = FragmentNomenclatureList.newInstance(id)
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.container_activity_main_1, fragment)
//            .addToBackStack("FRAGMENT_NOMENCLATURE_LIST")
//            .commit()
    }

    //    //проброска клика по позиции номенклатуры в списке, открывает карточку номенклатуры на всю страничку
    override fun onNomenclatureSelected(nomenclature: Nomenclature) {
//        val fragment = FragmentNomenclatureItem.newInstance(nomenclature)
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.container_activity_main_1, fragment)
//            .addToBackStack("FRAGMENT_NOMENCLATURE")
//            .commit()
    }

    override fun onLoginClicked(login: LoginBody) {
        val token: LiveData<ServerToken> = NordsideRepository().login(login)
        token.observe(this,
            Observer {
                Toast.makeText(this, token.value?.token, Toast.LENGTH_SHORT).show()
                appSettins.edit().putString(ApplicationConstants().TOKEN, token.value?.token)
                    .apply()
            })

//        //открываем фрагмент
//        val fragment = FragmentCommon.newInstance()
//        Log.v(TAG, "FRAGMENT_COMMON")
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.container_activity_main_1, fragment, "FRAGMENT_COMMON")
//            .addToBackStack("FRAGMENT_COMMON")
//            .commit()

    }

    override fun onRegistrationClicked(login: LoginBody) {

    }

}