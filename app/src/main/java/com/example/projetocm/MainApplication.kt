package com.example.projetocm

import android.app.Application
import com.example.projetocm.data.AppContainer
import com.example.projetocm.data.AppDataContainer

class MainApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}