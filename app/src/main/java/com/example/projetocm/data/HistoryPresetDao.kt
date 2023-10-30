package com.example.projetocm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HistorySessionDao {

    @Upsert
    suspend fun upsertSession(session: HistorySession)

    @Delete
    suspend fun deleteSession(session: HistorySession)

    @Query("SELECT * FROM HistorySessions WHERE id = :id")
    fun getSession(id: Int): Flow<HistorySession>

    @Query("SELECT * FROM HistorySessions")
    fun getAllSessions(): Flow<List<HistorySession>>
}