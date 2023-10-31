package com.example.projetocm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RunPresets")
data class RunPreset (
    val name: String,
    val seconds: Int,
    val km: Int,
    val twoWay: Boolean,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

val RunPresets = listOf<RunPreset>(
    RunPreset("Diario",350,15,false),
    RunPreset("Sofrimento",5455,50,true),
    RunPreset("preset",3020,30,true)
)