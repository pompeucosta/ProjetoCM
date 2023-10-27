package com.example.projetocm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RunPresets")
data class RunPreset (
    val seconds: Int,
    val km: Int,
    val twoWay: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

val RunPresets = listOf<RunPreset>(
    RunPreset(350,15,false),
    RunPreset(5455,50,true),
    RunPreset(3020,30,true)
)