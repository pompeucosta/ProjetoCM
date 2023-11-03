package com.example.projetocm.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.projetocm.MainActivity
import com.example.projetocm.NotificationIDs
import com.example.projetocm.R

class RunningService: Service() {

    private var isFirstRun = true

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.Start.toString() ->
                if(isFirstRun) {
                    start(intent.getStringExtra("message") ?: "")
                    isFirstRun = false
                }
            Actions.Stop.toString() -> {
                stopSelf()
                Log.d("t","stop")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(message: String) {
        val notification = NotificationCompat.Builder(this,"running_foreground_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Session goal")
            .setContentText(message)
            .setOngoing(true)
            .setAutoCancel(false)
            .setContentIntent(getMainActivityPendingIntent())
            .build()
        ServiceCompat.startForeground(this,NotificationIDs.Running_Service_ID,notification,
            if(Build.VERSION.SDK_INT >= 34)
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            else 0
        )
        //startForeground(NotificationIDs.Running_Service_ID,notification)
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this,MainActivity::class.java).also {
            it.action = Actions.Show.toString()
        },
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    enum class Actions {
        Start,
        Stop,
        Show
    }
}