package com.example.projetocm.ui.screens.session

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetocm.data.HistorySession
import com.example.projetocm.data.repositories.HistorySessionsRepository
import com.example.projetocm.ui.screens.history.HistorySessionUIInfo
import com.example.projetocm.ui.screens.history.toUIInfo
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class SessionEndDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val historySessionsRepository: HistorySessionsRepository
): ViewModel() {

    var historySession by mutableStateOf(HistorySessionUIInfo())

    private val sessionId = savedStateHandle["id"] ?: -1

    init {
        if(sessionId >= 0) {
            viewModelScope.launch {
                historySession = historySessionsRepository.getSessionStream(sessionId)
                    .filterNotNull()
                    .first()
                    .toUIInfo()
                Log.d("History session","${historySession}, ${historySession.sessionDetails.coordinates}")
            }
        }
    }


    fun getPoints(): List<LatLng>{
        Log.d("SessionEnd ","${getStartingPosition()}, ${getLastPosition()}")
        Log.d("SessionEnd ","${historySession.sessionDetails.coordinates}")
        val points: MutableList<LatLng> = mutableListOf()
        historySession.sessionDetails.coordinates.forEach{points.add(it.getLatLng())}
        return points
    }

    fun getStartingPosition(): LatLng{
        if(historySession.sessionDetails.coordinates.size > 0){
            return  historySession.sessionDetails.coordinates.first().getLatLng()
        }
        return LatLng(0.0,0.0)
    }

    fun getLastPosition(): LatLng{
        if(historySession.sessionDetails.coordinates.size > 0){
            return  historySession.sessionDetails.coordinates.last().getLatLng()
        }
        return LatLng(0.0,0.0)
    }
}