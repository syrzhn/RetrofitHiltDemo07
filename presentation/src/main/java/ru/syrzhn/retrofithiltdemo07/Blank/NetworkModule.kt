package ru.syrzhn.retrofithiltdemo07.Blank

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
class NetworkModule {

    @Provides
    fun provideNetworkUtils(app: Application, activity: Activity, fragment: Fragment): NetworkUtils {
        return NetworkUtils()
    }

}