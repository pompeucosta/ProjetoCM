package com.example.projetocm.data

import android.content.Context
import com.example.projetocm.data.repositories.HistorySessionsRepository
import com.example.projetocm.data.repositories.RunPresetsRepository


interface AppContainer {
    val presetsRepository: RunPresetsRepository
    val sessionsRepository: HistorySessionsRepository
}

class AppDataContainer(private val context: Context): AppContainer {

    override val presetsRepository: RunPresetsRepository by lazy {
        RunPresetsRepository(ProjetoDatabase.getDatabase(context).runPresetDao())
    }

    override val sessionsRepository: HistorySessionsRepository by lazy {
        HistorySessionsRepository(ProjetoDatabase.getDatabase(context).historySessionDao())
    }

}