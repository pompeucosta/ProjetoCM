package com.example.projetocm.ui

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projetocm.MainApplication
import com.example.projetocm.services.RunningService
import com.example.projetocm.ui.screens.history.HistoryViewModel
import com.example.projetocm.ui.screens.home.HomeViewModel
import com.example.projetocm.ui.screens.savedRuns.CreateRunViewModel
import com.example.projetocm.ui.screens.savedRuns.SavedRunsViewModel
import com.example.projetocm.ui.screens.session.SessionEndDetailsViewModel
import com.example.projetocm.ui.screens.session.SessionInProgressViewModel

object AppViewModelProvider{
    val Factory = viewModelFactory {
        initializer {
            CreateRunViewModel(mainApplication().container.presetsRepository,this.createSavedStateHandle())
        }

        initializer {
            SavedRunsViewModel(mainApplication().container.presetsRepository)
        }

        initializer {
            mainApplication().sessionInProgressViewModel
        }

        initializer {
            HistoryViewModel(mainApplication().container.sessionsRepository)
        }

        initializer {
            SessionEndDetailsViewModel(this.createSavedStateHandle(),mainApplication().container.sessionsRepository)
        }

        initializer {
            HomeViewModel(mainApplication().container.sessionsRepository)
        }
    }
}

fun CreationExtras.mainApplication(): MainApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)