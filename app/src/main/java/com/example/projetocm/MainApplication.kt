package com.example.projetocm

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPasteSearch
import androidx.core.app.NotificationCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetocm.data.AppContainer
import com.example.projetocm.data.AppDataContainer
import com.example.projetocm.services.RunningService
import com.example.projetocm.ui.AppViewModelProvider
import com.example.projetocm.ui.mainApplication
import com.example.projetocm.ui.screens.session.SessionInProgressViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule

object NotificationIDs {
    const val Running_Service_ID = 1
}

class MainApplication: Application() {
    lateinit var container: AppContainer
    //val runningService = RunningService()
    private var pushNotificationIds = NotificationIDs.Running_Service_ID + 1
    lateinit  var sessionInProgressViewModel: SessionInProgressViewModel


    override fun onCreate() {
        super.onCreate()
        Log.d("t","created")
        container = AppDataContainer(this)

        sessionInProgressViewModel = SessionInProgressViewModel(
            setForegroundService = {message ->
                Intent(this,RunningService::class.java).also {
                    it.putExtra("message",message)
                    it.action = RunningService.Actions.Start.toString()
                    startService(it)
                }
            },
            stopForegroundService = {
                Intent(this,RunningService::class.java).also {
                    it.action = RunningService.Actions.Stop.toString()
                    startService(it)
                }
            },
            updateForegroundMessage = {updateRunningServiceMessage(it)},
            sendNotification = {sendNotification(it)},
            container.sessionsRepository,
            container.presetsRepository
        )

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

        val serviceChannel = NotificationChannel(
            "running_channel",
            "RunRoute Notification",
            NotificationManager.IMPORTANCE_LOW
        )



        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        notificationManager.createNotificationChannel(serviceChannel)
    }

    fun sendNotification(message: String) {
        val notification = NotificationCompat.Builder(this,"session_channel")
            .setContentText(message)
            .setContentTitle("Session goal")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(pushNotificationIds,notification)
        pushNotificationIds += 1
    }

    fun updateRunningServiceMessage(message: String) {
        val notification = NotificationCompat.Builder(this,"running_channel")
            .setContentText(message)
            .setContentTitle("Session goal")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setContentIntent(getMainActivityPendingIntent())
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NotificationIDs.Running_Service_ID,notification)
    }

    fun changeRunId(runId: Int) {
        sessionInProgressViewModel.changeRunId(runId)
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this,MainActivity::class.java).also {
            it.action = RunningService.Actions.Show.toString()
        },
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}