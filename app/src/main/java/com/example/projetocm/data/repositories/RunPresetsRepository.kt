package com.example.projetocm.data.repositories

import com.example.projetocm.data.RunPreset
import com.example.projetocm.data.RunPresetDao
import kotlinx.coroutines.flow.Flow

class RunPresetsRepository(private val presetsDao: RunPresetDao): IRunPresetsRepository {
    override suspend fun upsertPreset(preset: RunPreset) = presetsDao.upsertPreset(preset)

    override suspend fun deletePreset(preset: RunPreset) = presetsDao.deletePreset(preset)

    override fun getPresetStream(id: Int): Flow<RunPreset?> = presetsDao.getPreset(id)

    override fun getAllPresetsStream(): Flow<List<RunPreset>> = presetsDao.getAllPresets()
}