package com.example.nordside_mobile.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.nordside_mobile.AppPreference
import com.example.nordside_mobile.R
import com.example.nordside_mobile.databinding.ActivityMainBinding
import com.example.nordside_mobile.model.Category
import com.example.nordside_mobile.model.LoginBody
import com.example.nordside_mobile.model.NomenclatureCollection
import com.example.nordside_mobile.model.PriceTable
import com.example.nordside_mobile.usecases.AccessTokenUseCase
import com.example.nordside_mobile.usecases.ApplicationConstants
import com.example.nordside_mobile.usecases.RefreshTokenUseCase
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.prefs.Preferences
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FragmentCategory.Callback, FragmentCollection.Callback,
    FragmentNomenclatureList.CallbackNomenclature, FragmentLogin.Callback, BottomNavigationButtonCallback,
FragmentRegister.Callback{

    private var TAG = "${MainActivity::class.simpleName} ###"
    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var navView: BottomNavigationView

    private var currentFragment: Fragment? = null

    @Inject
    lateinit var  appPreferences: AppPreference

//    private val appPreference: SharedPreferences = this.getSharedPreferences(
//        ApplicationConstants().SHARED_PREFERENCES_FILE,
//        Context.MODE_PRIVATE
//    )

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.also { it.setDisplayShowTitleEnabled(false) }

        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)

        checkTokenExpired()

    }

//    private val navListener =  View.OnClickListener { item ->
//            Log.v(TAG,item.id.toString())
//        }

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
    override fun onNomenclatureSelected(nomenclatureWithPrice: PriceTable) {
        launchDestination(
            R.id.fragmentNomenclatureItem,
            FragmentNomenclatureItem.createArgs(nomenclatureWithPrice)
        )
    }

    override fun onLoginClicked(isCorrectLogin: Boolean) {
        if (isCorrectLogin) {
            launchDestination(R.id.fragmentPersonal)
        } else {
            launchDestination(R.id.fragmentForgotPass)
        }
    }

    override fun onRegistrationClicked(loginBody: LoginBody) {
        launchDestination(
            R.id.fragmentRegister,
            FragmentLogin.createArgs(loginBody)
        )
    }

    private fun updateUi() {
        when(currentFragment) {
            is FragmentNomenclatureList, is FragmentNomenclatureItem,
            is FragmentCollection, is FragmentRegister -> {
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

    override fun setButtonVisible(buttonName: Int, isVisible: Boolean) {
        navView.menu.forEach { item ->
            if (item.title == getString(buttonName)) {
                item.isVisible = isVisible
                item.isChecked = isVisible
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkTokenExpired() {
        val refreshTokenUseCase =
            appPreferences.getSavedString(ApplicationConstants().REFRESH_TOKEN)
                ?.let { RefreshTokenUseCase(it) }
        if (refreshTokenUseCase != null) {
            if (refreshTokenUseCase.isExpared) {
                goToLoginPage()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun goToLoginPage() {
        Log.v(TAG, "launchRefreshToken() in MainActivity")

        launchDestination(R.id.fragmentLogin)
    }

    override fun onRegisterClicked() {
    }
}