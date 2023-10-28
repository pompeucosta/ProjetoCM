package com.example.projetocm

import android.app.Application
import com.example.projetocm.data.AppContainer
import com.example.projetocm.data.AppDataContainer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule

class MainApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(defaultModule)
        }
    }
}