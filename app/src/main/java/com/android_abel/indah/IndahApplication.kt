package com.android_abel.indah

import android.app.Application

class IndahApplication : Application() {

    companion object {
        private var instance: IndahApplication? = null

        fun applicationContext(): IndahApplication {
            if (instance == null) {
                instance = IndahApplication()
            }
            return instance as IndahApplication
        }
    }

    init {
        instance = this
    }


    override fun onCreate() {
        super.onCreate()
        //bussinesLinesList = mDatabaseRepository?.returnFunDBPrePopulate()!!
    }

}