package com.example.projetocm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RunPresetDao {

    @Upsert
    suspend fun upsertPreset(preset: RunPreset)

    @Delete
    suspend fun deletePreset(preset: RunPreset)

    @Query("SELECT * FROM RunPresets WHERE id = :id")
    fun getPreset(id: Int): Flow<RunPreset>

    @Query("SELECT * FROM RunPresets")
    fun getAllPresets() : Flow<List<RunPreset>>
}