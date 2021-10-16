package com.example.nordside_mobile

import android.app.Application
import android.content.Context
import com.example.nordside_mobile.repository.NordsideRepository

class MyApp : Application() {

    companion object {
        lateinit var instance: MyApp

        fun getContext(): Context? {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate();
        instance = this;

        //create repository singleton
        NordsideRepository.initialize(this)

    }
}
