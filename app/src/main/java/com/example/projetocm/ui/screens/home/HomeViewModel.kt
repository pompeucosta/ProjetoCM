package com.example.projetocm.ui.screens.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetocm.data.repositories.HistorySessionsRepository
import com.example.projetocm.ui.screens.history.HistorySessionUIInfo
import com.example.projetocm.ui.screens.history.toUIInfo
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(
    private val historySessionsRepository: HistorySessionsRepository
): ViewModel() {

    var historySession by mutableStateOf(HistorySessionUIInfo())
    var sessionExists by mutableStateOf(false)

    init {
        viewModelScope.launch {
            try {
                historySession = historySessionsRepository.getMostRecent()
                    .filterNotNull()
                    .first()
                    .toUIInfo()

                sessionExists = true
            } catch (ex: NoSuchElementException) {
                sessionExists = false
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

    fun getStartingPosition(): LatLng {
        if(historySession.sessionDetails.coordinates.size > 0){
            return  historySession.sessionDetails.coordinates.first().getLatLng()
        }
        return LatLng(0.0,0.0)
    }

    fun getLastPosition(): LatLng {
        if(historySession.sessionDetails.coordinates.size > 0){
            return  historySession.sessionDetails.coordinates.last().getLatLng()
        }
        return LatLng(0.0,0.0)
    }
}