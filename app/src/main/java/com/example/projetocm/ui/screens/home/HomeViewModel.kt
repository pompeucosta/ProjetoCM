package com.example.projetocm.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetocm.data.repositories.HistorySessionsRepository
import com.example.projetocm.ui.screens.history.HistorySessionUIInfo
import com.example.projetocm.ui.screens.history.toUIInfo
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
}