package com.example.projetocm.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory


/**
 * The rotationDegrees parameter is the rotation in degrees clockwise from the original orientation.
 */
fun Bitmap.rotateBitmap(rotationDegrees: Int): Bitmap {
    val matrix = Matrix().apply {
        postRotate(-rotationDegrees.toFloat())
        postScale(-1f, -1f)
    }

    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun BitmapFromVector(context: Context, vectorResId:Int): BitmapDescriptor? {
    //drawable generator
    var vectorDrawable: Drawable
    vectorDrawable= ContextCompat.getDrawable(context,vectorResId)!!
    vectorDrawable.setBounds(0,0,vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight)
    //bitmap generator
    var bitmap:Bitmap
    bitmap= Bitmap.createBitmap(vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
    //canvas generator
    var canvas: Canvas
    //pass bitmap in canvas constructor
    canvas= Canvas(bitmap)
    //pass canvas in drawable
    vectorDrawable.draw(canvas)
    //return BitmapDescriptorFactory
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}