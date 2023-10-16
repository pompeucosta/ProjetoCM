package com.example.projetocm.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Session(
    val location: String,
    val date: LocalDate,
    val km: Int,
    val time: String
)

val sessions = listOf(
    Session("Aveiro",LocalDate.parse("25-08-2022", DateTimeFormatter.ofPattern("dd-MM-yyyy")),10,"32min"),
    Session("Coimbra",LocalDate.parse("21-08-2022", DateTimeFormatter.ofPattern("dd-MM-yyyy")),30,"1h 38min"),
    Session("São Bernardo",LocalDate.parse("15-08-2022", DateTimeFormatter.ofPattern("dd-MM-yyyy")),5,"10min"),
    Session("Aveiro",LocalDate.parse("07-08-2022", DateTimeFormatter.ofPattern("dd-MM-yyyy")),18,"40min"),
    Session("Porto",LocalDate.parse("29-07-2022", DateTimeFormatter.ofPattern("dd-MM-yyyy")),8,"15min")
)