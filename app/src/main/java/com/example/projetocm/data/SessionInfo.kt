package com.example.projetocm.data

data class SessionInfo(
    val seconds: Long = 0,
    val distance: Float = 0f,
    val topSpeed: Float = 0f,
    val stepsTaken : Int = 0,
    val averageSpeed: Float = 0f,
    val calories: Int = 0,
    val coordinates: List<PathPoint> = emptyList()
)
