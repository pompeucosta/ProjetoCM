package com.example.projetocm.ui.screens.session

import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.projetocm.data.HistorySession
import com.example.projetocm.data.SessionInfo
import com.example.projetocm.data.repositories.HistorySessionsRepository
import java.time.LocalDate

data class SessionInfoUI(
    val sessionInfoDetails: SessionInfoDetails = SessionInfoDetails(),
    val paused: Boolean = false,
)

data class SessionInfoDetails(
    val time: String = "00:00:00",
    val distance: String = "0",
    val topSpeed: String = "0",
    val stepsTaken : String = "0",
    val averageSpeed: String = "0",
    val calories: String = "0"
)

fun SessionInfo.toUIInfo(): SessionInfoUI = SessionInfoUI(sessionInfoDetails =  this.toUIDetails())

fun SessionInfo.toUIDetails(): SessionInfoDetails {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val seconds = seconds % 60
    return SessionInfoDetails("$hours:$minutes:$seconds",distance.toString(),topSpeed.toString(),stepsTaken.toString(),averageSpeed.toString(),calories.toString())
}

fun SessionInfoUI.toSessionInfo(): SessionInfo {
    val parts = sessionInfoDetails.time.split(":")
    val hours = parts[0].toLong()
    val minutes = parts[1].toLong()
    val seconds = parts[2].toLong()

    val totalSeconds = hours * 3600 + minutes * 60 + seconds

    return SessionInfo(seconds= totalSeconds,distance= sessionInfoDetails.distance.toFloatOrNull() ?: 0f, topSpeed = sessionInfoDetails.topSpeed.toFloatOrNull() ?: 0f, stepsTaken = sessionInfoDetails.stepsTaken.toIntOrNull() ?: 0
    , averageSpeed = sessionInfoDetails.averageSpeed.toFloatOrNull() ?: 0f, calories = sessionInfoDetails.calories.toIntOrNull() ?: 0)
}

class SessionInProgressViewModel(private val historySessionsRepository: HistorySessionsRepository) : ViewModel() {
    var sessionInfoUI by mutableStateOf(SessionInfoUI())
        private set

    //private val coordinates: List<String> = emptyList() //lista de coordenadas da localizacao (trocar de string para o tipo certo)

    private var startTime: Long = 0
    private var location = "" //localizacao tipo Aveiro ou Porto
    private val today = LocalDate.now()

    private val timer: CountDownTimer = object : CountDownTimer(Long.MAX_VALUE,1000) {
        override fun onTick(millisUntilFinished: Long) {
            updateElapsedTime()
        }

        override fun onFinish() { }
    }

    init {
        startTimer()
    }

    private fun startTimer() {
        startTime = SystemClock.elapsedRealtime()
        timer.start()
    }

    fun pauseUnpauseClick() {
        if(sessionInfoUI.paused)
            unpause()
        else
            pause()
    }

    suspend fun finishSession() {
        val sessionInfo = sessionInfoUI.toSessionInfo()
        val historySession = HistorySession(
            location= location,
            day= today.dayOfMonth,
            month= today.monthValue,
            year= today.year,
            sessionInfo = sessionInfo
        )
        historySessionsRepository.upsertSession(historySession)
    }

    private fun pause() {
        sessionInfoUI = sessionInfoUI.copy(paused = true)
        timer.cancel()
    }

    private fun unpause() {
        sessionInfoUI = sessionInfoUI.copy(paused= false)
        startTimer()
    }

    private fun updateElapsedTime() {
        val currentTime = SystemClock.elapsedRealtime()
        val elapsedTime = (currentTime - startTime) / 1000
        val hours = elapsedTime / 3600
        val minutes = (elapsedTime % 3600) / 60
        val seconds = elapsedTime % 60
        Log.d("time elapsed",elapsedTime.toString())
        Log.d("time",startTime.toString())
        Log.d("time",seconds.toString())
        sessionInfoUI = sessionInfoUI.copy(sessionInfoDetails = sessionInfoUI.sessionInfoDetails.copy(time= String.format("%02d:%02d:%02d", hours, minutes, seconds)))
    }
}