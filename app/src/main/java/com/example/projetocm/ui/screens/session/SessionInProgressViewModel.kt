package com.example.projetocm.ui.screens.session

import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetocm.data.HistorySession
import com.example.projetocm.data.RunPreset
import com.example.projetocm.data.SessionInfo
import com.example.projetocm.data.repositories.HistorySessionsRepository
import com.example.projetocm.data.repositories.RunPresetsRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

class SessionInProgressViewModel(
    private val setForegroundService: (String) -> Unit,
    private val stopForegroundService: () -> Unit,
    private val updateForegroundMessage: (String) -> Unit,
    private val sendNotification: (String) -> Unit,
    private val historySessionsRepository: HistorySessionsRepository,
    private val runsRepository: RunPresetsRepository
) : ViewModel() {
    var sessionInfoUI by mutableStateOf(SessionInfoUI())
        private set

    private var runId = -1
    private var goalRun: RunPreset = RunPreset("",0,0,false)
    //private val coordinates: List<String> = emptyList() //lista de coordenadas da localizacao (trocar de string para o tipo certo)

    private var startTime: Long = 0
    private var location = "" //localizacao tipo Aveiro ou Porto
    private val today = LocalDate.now()

    private var timeWarned = false
    private var hasPermission = false
    private var distanceWarned = false
    private var timerStarted = false
    private var time: Long = 0
    private var elapsedTime: Long = 0
    private var foregroundServiceLaunched = false


    private val timer: CountDownTimer = object : CountDownTimer(Long.MAX_VALUE,1000) {
        override fun onTick(millisUntilFinished: Long) {
            updateElapsedTime()
        }

        override fun onFinish() { }
    }

    fun pauseUnpauseClick() {
        if(runId > 0) {
            if (sessionInfoUI.paused)
                unpause()
            else
                pause()
        }
    }

    fun updatePermission(permission: Boolean) {
        hasPermission = permission
    }

    fun startTimer() {
        if(!timerStarted && runId > 0) {
            timerStarted = true
            startTime = SystemClock.elapsedRealtime()
            timer.start()
        }

        if(hasPermission && !foregroundServiceLaunched && runId > 0){
            setForegroundService("test")
            Log.d("t","started service")
            foregroundServiceLaunched = true
        }


    }

    fun changeRunId(_runId: Int) {
        if(runId > 0) {
            reset()
        }

        runId = _runId
        if(runId > 0) {
            viewModelScope.launch {
                goalRun = runsRepository.getPresetStream(runId)
                    .filterNotNull()
                    .first()
            }
        }
    }

    private fun reset() {
        runId = -1
        timerStarted = false
        if(sessionInfoUI.paused) {
            timer.cancel()
        }
        sessionInfoUI = SessionInfoUI()
        if(foregroundServiceLaunched) {
            stopForegroundService()
            foregroundServiceLaunched = false
        }
    }

    suspend fun finishSession(): Int {
        if(runId > 0) {
            val sessionInfo = sessionInfoUI.toSessionInfo()
            val historySession = HistorySession(
                location = location,
                day = today.dayOfMonth,
                month = today.monthValue,
                year = today.year,
                sessionInfo = sessionInfo
            )
            historySessionsRepository.upsertSession(historySession)
            val session = historySessionsRepository.getMostRecent()
                .filterNotNull()
                .first()

            reset()
            return session.id
        }

        return -1
    }

    private fun pause() {
        sessionInfoUI = sessionInfoUI.copy(paused = true)
        timer.cancel()
        time = elapsedTime
    }

    private fun unpause() {
        sessionInfoUI = sessionInfoUI.copy(paused = false)
        startTime = SystemClock.elapsedRealtime()
        timer.start()
    }

    private fun updateElapsedTime() {
        val currentTime = SystemClock.elapsedRealtime()
        elapsedTime = ((currentTime - startTime) / 1000) + time //total seconds since start
        val hours = elapsedTime / 3600
        val minutes = (elapsedTime % 3600) / 60
        val seconds = elapsedTime % 60

        sessionInfoUI = sessionInfoUI.copy(sessionInfoDetails = sessionInfoUI.sessionInfoDetails.copy(time= String.format("%02d:%02d:%02d", hours, minutes, seconds)))

        if(hasPermission && !timeWarned && elapsedTime >= goalRun.seconds) {
            //send notification
            sendNotification("You have reached the time goal!!")
            timeWarned = true
        }

        updateNotification()

    }

    private fun updateNotification() {
        if(hasPermission && foregroundServiceLaunched) {
            updateForegroundMessage(sessionInfoUI.sessionInfoDetails.time)

            if(!distanceWarned && (sessionInfoUI.sessionInfoDetails.distance.toIntOrNull() ?: 0) >= goalRun.km) {
                sendNotification("You have completed your distance goal!!")
                distanceWarned = true
            }
        }
    }
}