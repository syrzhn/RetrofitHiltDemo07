package ru.syrzhn.retrofithiltdemo07

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
import ru.syrzhn.retrofithiltdemo07.Blank.DatabaseHelper
import ru.syrzhn.retrofithiltdemo07.Blank.NetworkUtils

@HiltAndroidApp
class App: Application() {

    @Inject
    lateinit var databaseHelper: DatabaseHelper

    lateinit var networkUtils: NetworkUtils

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "databaseHelper = $databaseHelper")
    }
}

const val TAG = "RetrofitHiltDemo07"