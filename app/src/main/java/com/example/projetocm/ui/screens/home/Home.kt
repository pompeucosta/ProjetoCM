package com.example.projetocm.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetocm.R
import com.example.projetocm.ui.AppViewModelProvider
import com.example.projetocm.ui.screens.session.SessionInfo
import com.example.projetocm.ui.theme.ProjetoCMTheme
import com.example.projetocm.ui.utils.BitmapFromVector
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun Home(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory= AppViewModelProvider.Factory)
) {
    Column(
        modifier= modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(viewModel.sessionExists) {
            Text(
                text= stringResource(id = R.string.your_last_session),
                style= MaterialTheme.typography.displayMedium
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
            ) {
                var uiSettings by remember { mutableStateOf(
                    MapUiSettings(
                        compassEnabled = false,
                        zoomControlsEnabled = false
                    )
                ) }
                var mapProperties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }
                val baseZoom = 16f
                var currentPosition = viewModel.getLastPosition()
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(currentPosition, baseZoom)
                }
                if(currentPosition.latitude != 0.0 && currentPosition.longitude != 0.0){
                    cameraPositionState.position = CameraPosition.fromLatLngZoom(currentPosition, baseZoom)
                }

                GoogleMap(
                    properties = mapProperties,
                    uiSettings= uiSettings,
                    modifier = Modifier
                        .padding(10.dp),
                    cameraPositionState = cameraPositionState
                ){
                    Polyline(points = viewModel.getPoints())
                    if(viewModel.getStartingPosition().latitude != 0.0 && viewModel.getStartingPosition().longitude != 0.0 ){
                        Marker(
                            state= MarkerState(viewModel.getStartingPosition()),
                            title= "Start",
                            icon= BitmapFromVector(LocalContext.current,R.drawable.baseline_flag_24)
                        )
                    }


                    if(viewModel.getLastPosition().latitude != 0.0 && viewModel.getLastPosition().longitude != 0.0 ){
                        Marker(
                            state= MarkerState(viewModel.getLastPosition()),
                            title= "Start",
                            icon= BitmapFromVector(LocalContext.current,R.drawable.baseline_directions_run_24)
                        )
                    }

                }
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
        else {
            Text(
                text= stringResource(id = R.string.no_session),
                style= MaterialTheme.typography.displayMedium
            )

            Text(
                text= stringResource(id = R.string.start_running),
                style= MaterialTheme.typography.displayMedium
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    Scaffold {
        Home(modifier= Modifier.padding(it))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomePreviewDark() {
    ProjetoCMTheme(darkTheme = true) {
        Scaffold {
            Home(modifier= Modifier.padding(it))
        }
    }

}