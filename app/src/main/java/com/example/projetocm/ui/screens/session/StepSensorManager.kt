package com.example.projetocm.ui.screens.session

import android.content.Context;
import android.content.Context.SENSOR_SERVICE
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager;
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class StepSensorManager: AppCompatActivity(),SensorEventListener {

    private var currentSteps = 0f

    fun getCurrentSteps(): Float{
        return currentSteps
    }

    override fun onSensorChanged(event: SensorEvent){
        Log.d("Steps sensor", "sensor steps: ${currentSteps}")
        event ?: return
        event.values.firstOrNull()?.let {
            if(it > 0){
                    currentSteps = it
            }

        }
    }

    override fun onAccuracyChanged(sensor: Sensor, p1: Int){

    }

}


/*
public class StepSensorManage implements SensorEventListener  {
    private static SensorManager sensorManager;
    private final Context context;

    public ShakeEvent(Context context) {
        this.context = context;
    }

    ...
    ...
    public static boolean isSupported (){
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);*/