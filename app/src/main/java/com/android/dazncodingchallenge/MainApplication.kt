package com.android.dazncodingchallenge

import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.multidex.MultiDex
import com.android.dazncodingchallenge.utils.NetworkMonitor
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application(){

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        NetworkMonitor(this).startNetworkCallback()
    }


    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        // Stop network callback
        NetworkMonitor(this).stopNetworkCallback()
    }


}