package com.example.projetocm

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.example.projetocm.data.AppContainer
import com.example.projetocm.data.AppDataContainer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule

class MainApplication: Application() {
    lateinit var container: AppContainer
    lateinit var appContext: Context

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        appContext = this

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(defaultModule)
        }

        val channel = NotificationChannel(
            "session_channel",
            "RunRoute Notification",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}