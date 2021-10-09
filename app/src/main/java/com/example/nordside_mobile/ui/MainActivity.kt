package com.nordside_trading.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nordside_mobile.R
import com.example.nordside_mobile.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nordside_trading.ApplicationConstants
import com.nordside_trading.R
import com.nordside_trading.databinding.ActivityMainBinding
import com.nordside_trading.model.LoginBody
import com.nordside_trading.model.Nomenclature
import com.nordside_trading.model.ServerToken
import com.nordside_trading.repository.NordsideRepository

class MainActivity : AppCompatActivity(), FragmentCategory.Callback, FragmentCollection.Callback,
    FragmentNomenclatureList.CallbackNomenclature, FragmentLogin.Callback {

    private var TAG = MainActivity::class.simpleName

    lateinit var appSettins: SharedPreferences

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_login, R.id.navigation_cart
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


//        val currentFragment = supportFragmentManager.findFragmentById(R.id.container_activity_main_1)
//        if (currentFragment==null) {
//            supportFragmentManager
//                .beginTransaction()
//                .add(R.id.container_activity_main_1, FragmentLogin.newInstance(), "FRAGMENT_LOGIN")
//                //.add(R.id.container_activity_main_1, FragmentCommon.newInstance(), "FRAGMENT_COMMON")
//                .addToBackStack("FRAGMENT_LOGIN")
//                .commit()
//        }

        //инициализируем файл для сохраниния параметров приложения
       appSettins = getSharedPreferences(ApplicationConstants().SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)

    }
    //проброска клика по категории во фрагмент
    override fun onCategorySelected(id: String) {
//        //Log.v(TAG,id)
//        val fragment = supportFragmentManager.findFragmentByTag("FRAGMENT_COMMON") as FragmentCommon
//        fragment.onCategorySelected(id)
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
                Toast.makeText(this, token.value?.token,Toast.LENGTH_SHORT).show()
                appSettins.edit().putString(ApplicationConstants().TOKEN,token.value?.token).apply()



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