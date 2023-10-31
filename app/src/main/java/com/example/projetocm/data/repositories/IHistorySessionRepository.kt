package com.example.projetocm.data.repositories

import com.example.projetocm.data.HistorySession
import kotlinx.coroutines.flow.Flow

interface IHistorySessionRepository {
    suspend fun upsertSession(session: HistorySession)

    suspend fun deleteSession(session: HistorySession)

    fun getSessionStream(id: Int): Flow<HistorySession>

    fun getAllSessionsStream(): Flow<List<HistorySession>>

    fun getMostRecent(): Flow<HistorySession>
}