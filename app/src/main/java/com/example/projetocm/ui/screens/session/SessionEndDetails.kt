package com.example.projetocm.ui.screens.session

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetocm.ui.AppViewModelProvider
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings

@Composable
fun SessionEndDetails(
    modifier: Modifier = Modifier,
    viewModel: SessionEndDetailsViewModel = viewModel(factory= AppViewModelProvider.Factory)
) {
    Column(
        modifier= modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
        ) {
            var uiSettings by remember { mutableStateOf(
                MapUiSettings(
                compassEnabled = false,
                //zoomControlsEnabled = false
            )
            ) }
            var mapProperties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }

            /*GoogleMap(
                properties = mapProperties,
                uiSettings= uiSettings,
                modifier = Modifier
                    .padding(10.dp)
            )*/
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
        ) {

            Text(
                text = viewModel.historySession.location,
                style = MaterialTheme.typography.displayLarge,
            )

            Text(
                text = viewModel.historySession.date,
                style= MaterialTheme.typography.labelSmall,
            )

            SessionInfo(
                modifier = Modifier
                    .fillMaxWidth(),
                sessionInfoDetails = viewModel.historySession.sessionDetails
            )

            // mostrar as fotografias aqui em baixo numa lazy column

        }
    }
}