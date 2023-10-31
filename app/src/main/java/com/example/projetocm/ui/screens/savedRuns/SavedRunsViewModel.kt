package com.example.projetocm.ui.screens.savedRuns

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetocm.data.RunPreset
import com.example.projetocm.data.repositories.RunPresetsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class SavedRunsUIState(val presetList: List<RunPreset> = listOf())

class SavedRunsViewModel(private val runPresetsRepository: RunPresetsRepository): ViewModel() {
    val savedRunsUIState: StateFlow<SavedRunsUIState> = runPresetsRepository.getAllPresetsStream().map { SavedRunsUIState(it) }
        .stateIn(
            scope=viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SavedRunsUIState()
        )

    suspend fun delete(preset: RunPreset) {
        runPresetsRepository.deletePreset(preset)
    }

    suspend fun save(preset: RunPreset) {
        runPresetsRepository.upsertPreset(preset)
    }
}