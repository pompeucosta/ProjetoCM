package com.example.projetocm.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projetocm.MainApplication
import com.example.projetocm.ui.screens.history.HistoryViewModel
import com.example.projetocm.ui.screens.savedRuns.CreateRunViewModel
import com.example.projetocm.ui.screens.savedRuns.SavedRunsViewModel
import com.example.projetocm.ui.screens.session.SessionInProgressViewModel

object AppViewModelProvider{
    val Factory = viewModelFactory {
        initializer {
            CreateRunViewModel(mainApplication().container.presetsRepository)
        }

        initializer {
            SavedRunsViewModel(mainApplication().container.presetsRepository)
        }

        initializer {
            SessionInProgressViewModel(mainApplication().container.sessionsRepository)
        }

        initializer {
            HistoryViewModel(mainApplication().container.sessionsRepository)
        }
    }
}

fun CreationExtras.mainApplication(): MainApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)