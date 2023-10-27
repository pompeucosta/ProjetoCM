package com.example.projetocm.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projetocm.MainApplication
import com.example.projetocm.ui.screens.savedRuns.CreateRunViewModel
import com.example.projetocm.ui.screens.savedRuns.SavedRunsViewModel

object AppViewModelProvider{
    val Factory = viewModelFactory {
        initializer {
            CreateRunViewModel(mainApplication().container.presetsRepository)
        }

        initializer {
            SavedRunsViewModel(mainApplication().container.presetsRepository)
        }
    }
}

fun CreationExtras.mainApplication(): MainApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MainApplication)