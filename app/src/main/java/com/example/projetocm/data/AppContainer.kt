package com.example.projetocm.data

import android.content.Context
import com.example.projetocm.data.repositories.RunPresetsRepository


interface AppContainer {
    val presetsRepository: RunPresetsRepository
}

class AppDataContainer(private val context: Context): AppContainer {

    override val presetsRepository: RunPresetsRepository by lazy {
        RunPresetsRepository(ProjetoDatabase.getDatabase(context).runPresetDao())
    }
}