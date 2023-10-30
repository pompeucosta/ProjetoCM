package com.example.projetocm.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetocm.data.HistorySession
import com.example.projetocm.data.repositories.HistorySessionsRepository
import com.example.projetocm.ui.screens.session.SessionInfoDetails
import com.example.projetocm.ui.screens.session.toUIDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class SavedSessionsUIState(val sessionList: List<HistorySession> = listOf())

data class HistorySessionUIInfo(
    val location: String = "",
    val date: String = "",
    val sessionDetails: SessionInfoDetails = SessionInfoDetails()
)

fun HistorySession.toUIInfo(): HistorySessionUIInfo {
    val dateFormat = SimpleDateFormat("dd MMMMM yyyyy", Locale.getDefault())
    val cal = Calendar.getInstance()
    cal.set(year,month-1,day)
    return HistorySessionUIInfo(location = location, date =  dateFormat.format(cal.time), sessionDetails = sessionInfo.toUIDetails())
}

class HistoryViewModel(sessionsRepository: HistorySessionsRepository): ViewModel() {
    val savedSessionsUIState: StateFlow<SavedSessionsUIState> = sessionsRepository.getAllSessionsStream().map { SavedSessionsUIState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SavedSessionsUIState()
        )

}