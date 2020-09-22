package com.android_abel.indah

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import com.android_abel.indah.BuildConfig.APPLICATION_ID
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings


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
        if (!isMainProcess(this)) {
            FirebaseApp.initializeApp(this)
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build()
            FirebaseFirestore.getInstance().firestoreSettings = settings
            // other things
            return
        }

        //bussinesLinesList = mDatabaseRepository?.returnFunDBPrePopulate()!!
    }

    private fun isMainProcess(context: Context?): Boolean {
        if (null == context) {
            return true
        }
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val pid = Process.myPid()
        for (processInfo in manager.runningAppProcesses) {
            if (APPLICATION_ID == processInfo.processName && pid == processInfo.pid) {
                return true
            }
        }
        return false
    }
}