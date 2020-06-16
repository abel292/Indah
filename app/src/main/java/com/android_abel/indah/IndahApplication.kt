package com.android_abel.indah

import android.app.Application

class IndahApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: IndahApplication? = null

        fun applicationContext(): IndahApplication {
            return instance as IndahApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        //bussinesLinesList = mDatabaseRepository?.returnFunDBPrePopulate()!!
    }

}