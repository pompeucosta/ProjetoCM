package com.example.projetocm.data

import com.google.android.gms.maps.model.LatLng

class PathPoint(latlng: LatLng, img: String, time: Long) {
    private var latLng: LatLng = latlng
    private var image = img //" " if there's no image
    private var timeReached = time

    fun getLatLng(): LatLng {
        return latLng
    }
    fun setLatLng(latlng: LatLng){
        latLng = latlng
    }

    fun getTime(): Long{
        return timeReached
    }

    fun getImagePath(): String{
        return image
    }
}