package com.example.projetocm.ui.screens.savedRuns

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projetocm.data.RunPreset
import com.example.projetocm.data.repositories.RunPresetsRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class RunPresetUIState(
    val presetDetails: RunPresetDetails = RunPresetDetails(),
    val isEntryValid: Boolean = false
)

data class RunPresetDetails(
    val hours: String = "0",
    val minutes: String = "0",
    val seconds: String = "0",
    val km: String = "0",
    val twoWay: Boolean = false,
    val id: Int = 0
)

fun RunPreset.toRunPresetDetails(): RunPresetDetails {
    val hours:Int  = seconds / 3600
    val minutes: Int = (seconds % 3600) / 60
    val seconds: Int = seconds % 60

    return RunPresetDetails(hours.toString(),minutes.toString(),seconds.toString(),km.toString(),twoWay,id)
}

fun RunPreset.toUIState(isEntryValid: Boolean): RunPresetUIState = RunPresetUIState(
    presetDetails = this.toRunPresetDetails(),
    isEntryValid = isEntryValid
)

fun RunPresetDetails.toRunPreset(): RunPreset {
    val hours = hours.toIntOrNull() ?: 0
    val minutes = minutes.toIntOrNull() ?: 0
    val seconds = seconds.toIntOrNull() ?: 0

    return RunPreset(hours * 3600 + minutes * 60 + seconds,km.toInt(),twoWay,id)
}

class CreateRunViewModel(
    private val presetsRepository: RunPresetsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val itemId: Int = savedStateHandle["id"] ?: -1

    var presetUIState by mutableStateOf(RunPresetUIState())
        private set

    init {
        if(itemId >= 0) {
            viewModelScope.launch {
                presetUIState = presetsRepository.getPresetStream(itemId)
                    .filterNotNull()
                    .first()
                    .toUIState(true)
            }
        }
    }

    suspend fun savePreset() {
        presetsRepository.upsertPreset(presetUIState.presetDetails.toRunPreset())
    }

    fun updateUIState(presetDetails: RunPresetDetails) {
        presetUIState = RunPresetUIState(presetDetails= presetDetails, isEntryValid = validateInput(presetDetails))
    }

    private fun validateInput(uiState: RunPresetDetails = presetUIState.presetDetails): Boolean {
        return with(uiState) {
            val h = hours.toIntOrNull() ?: 0
            val min = minutes.toIntOrNull() ?: 0
            val sec = seconds.toIntOrNull() ?: 0
            (km.toIntOrNull() ?: 0) > 0 && (h+min+sec) > 0
        }
    }
}