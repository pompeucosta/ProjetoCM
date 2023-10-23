package com.example.projetocm.data


data class RunPreset (
    val time: Time,
    val km: Int,
    val twoWay: Boolean
)

val RunPresets = listOf<RunPreset>(
    RunPreset(Time(0,10,33),15,false),
    RunPreset(Time(1,30,55),50,true),
    RunPreset(Time(0,50,20),30,true)
)