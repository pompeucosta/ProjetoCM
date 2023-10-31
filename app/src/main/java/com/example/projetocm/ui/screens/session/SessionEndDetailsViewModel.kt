package com.example.projetocm.ui.screens.session

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetocm.data.repositories.HistorySessionsRepository
import com.example.projetocm.ui.screens.history.HistorySessionUIInfo
import com.example.projetocm.ui.screens.history.toUIInfo
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
            }
        }
    }
}