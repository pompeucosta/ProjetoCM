package com.example.projetocm.data.repositories

import com.example.projetocm.data.RunPreset
import kotlinx.coroutines.flow.Flow

interface IRunPresetsRepository {
    suspend fun upsertPreset(preset: RunPreset)

    suspend fun deletePreset(preset: RunPreset)

    fun getPresetStream(id: Int): Flow<RunPreset?>

    fun getAllPresetsStream() : Flow<List<RunPreset>>
}