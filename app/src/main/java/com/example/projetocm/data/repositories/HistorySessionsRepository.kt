package com.example.projetocm.data.repositories

import com.example.projetocm.data.HistorySession
import com.example.projetocm.data.HistorySessionDao
import kotlinx.coroutines.flow.Flow

class HistorySessionsRepository(private val historySessionDao: HistorySessionDao): IHistorySessionRepository {

    override suspend fun deleteSession(session: HistorySession) = historySessionDao.deleteSession(session)

    override fun getAllSessionsStream(): Flow<List<HistorySession>> = historySessionDao.getAllSessions()

    override fun getSessionStream(id: Int): Flow<HistorySession> = historySessionDao.getSession(id)

    override suspend fun upsertSession(session: HistorySession) = historySessionDao.upsertSession(session)
}