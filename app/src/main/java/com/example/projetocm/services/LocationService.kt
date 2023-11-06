package com.example.projetocm.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.projetocm.MainActivity
import com.example.projetocm.NotificationIDs
import com.example.projetocm.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class LocationService: Service() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback

    private var currentLocation: Location? = null
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    override fun onCreate() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY,500).build()
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                Log.d("Location","${locationResult.lastLocation}")
                currentLocation = locationResult.lastLocation

                if(locationResult.lastLocation != null){
                    val intent = Intent("com.example.projetocm.LocationBroadcast")
                    intent.putExtra("Latitude", locationResult.lastLocation!!.latitude)
                    intent.putExtra("Longitude", locationResult.lastLocation!!.longitude)
                    intent.putExtra("Time", locationResult.lastLocation!!.time)
                    sendBroadcast(intent)
                }

            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            Actions.Start.toString() ->{
                startNotification()
                subscribeToLocationUpdates()}
            Actions.Stop.toString() -> {
                unsubscribeToLocationUpdates()
                stopSelf()
                Log.d("t", "stop")
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startNotification(){
        val notification = NotificationCompat.Builder(this,"location_foreground_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Tracking Location")
            .setContentText("")
            .setOngoing(true)
            .setAutoCancel(false)
            .setContentIntent(getMainActivityPendingIntent())
            .build()
        ServiceCompat.startForeground(this, NotificationIDs.Location_Service_ID,notification,
            if(Build.VERSION.SDK_INT >= 34)
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            else 0
        )
    }

    public fun getLastLocation(): Location?{
        return currentLocation
    }
    public fun subscribeToLocationUpdates(){
        startService(Intent(applicationContext, LocationService::class.java))
        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (unlikely: SecurityException) {
            Log.e("Location Service", "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    fun unsubscribeToLocationUpdates(){
        try {
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Location Service", "Location Callback removed.")
                    stopSelf()
                } else {
                    Log.d("Location Service", "Failed to remove Location Callback.")
                }
            }
        } catch (unlikely: SecurityException) {
            Log.e("Location Service", "Lost location permissions. Couldn't remove updates. $unlikely")
        }
    }

    inner class LocalBinder : Binder() {
        internal val service: LocationService
            get() = this@LocationService
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this,MainActivity::class.java).also {
            it.action = RunningService.Actions.Show.toString()
        },
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    enum class Actions {
        Start,
        Stop,
        Show
    }

}