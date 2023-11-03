package com.example.projetocm.data

import android.util.Log
import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng

class dbTypeConverters {

    //PathPints get stored as string in the format: latitude,longitude;image;timestamp
    @TypeConverter
    fun pathPointFromString(string: String): PathPoint? {
        var list = string.split(";")
        Log.d("Converter","PathPoint from String: ${list}, list size: ${list.size}")
        if(list.size == 3){
            val latlngValues = list[0].split(",")
            val image = list[1]
            val timestamp = list[2].toLong()
            if(latlngValues.size == 2){
                val latlng = LatLng(latlngValues[0].toDouble(),latlngValues[1].toDouble())
                return PathPoint(latlng,image,timestamp)
            }
        }
        return null
    }

    @TypeConverter
    fun pathPointToString(pathPoint: PathPoint): String {
        return "${pathPoint.getLatLng().latitude},${pathPoint.getLatLng().longitude};${pathPoint.getImagePath()};${pathPoint.getTime()}"
    }
    @TypeConverter
    fun pathPointListToString(list: List<PathPoint>): String {
        var string = ""
        list.forEach {
            string += "[${pathPointToString(it)}]"
        }
        Log.d("Converter","PathPoint list to String: ${string}, list size: ${list.size}")
        return string
    }
    @TypeConverter
    fun stringToPathPointList(string: String): List<PathPoint> {
        var list = mutableListOf<PathPoint>()
        if(string.length > 2){
            val stringList = string.substring(1,string.length-1).split("][")
            stringList.forEach {
                val pathpoint = pathPointFromString(it)
                if(pathpoint is PathPoint){
                    list.add(pathpoint)
                }
            }
        }
        Log.d("Converter","String to PathPoint list: ${list}, string lenght: ${string.length}")
        return list
    }

}