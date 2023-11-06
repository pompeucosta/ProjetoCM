package com.example.projetocm.ui.screens.session

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetocm.R
import com.example.projetocm.services.LocationService
import com.example.projetocm.ui.AppViewModelProvider
import com.example.projetocm.ui.theme.ProjetoCMTheme
import com.example.projetocm.ui.utils.BitmapFromVector
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch



@Composable
fun SessionInProgress(
    onNavigateToCamera: () -> Unit,
    modifier: Modifier = Modifier,
    onSessionEnd: (Int) -> Unit = {},
    onSessionCancel: () -> Unit = {},
    locationClient: FusedLocationProviderClient,
    viewModel: SessionInProgressViewModel = viewModel(factory= AppViewModelProvider.Factory)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        val coroutineScope = rememberCoroutineScope()

        val locationPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = {isGranted ->
                viewModel.updateLocationPermission(isGranted)
            }
        )

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && viewModel.hasLocationPermission()) {
            val context = LocalContext.current

            LaunchedEffect(locationPermissionLauncher) {
                when(PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) -> {
                        viewModel.updateLocationPermission(true)
                    }
                    else -> {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    }
                }
            }
        }
        else {
            viewModel.updateLocationPermission(true)
        }

        val bgLocationPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = {isGranted ->
                viewModel.updateLocationPermission(isGranted)
            }
        )

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && viewModel.hasLocationPermission()) {
            val context = LocalContext.current

            LaunchedEffect(locationPermissionLauncher) {
                when(PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) -> {
                    }
                    else -> {
                        bgLocationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    }
                }
            }
        }



        val notificationPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = {isGranted ->
                viewModel.updatePermission(isGranted)

            }
        )

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && viewModel.hasNotificationPermission()) {
            val context = LocalContext.current

            LaunchedEffect(notificationPermissionLauncher) {
                when(PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) -> {
                        viewModel.updatePermission(true)
                    }
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) -> {
                        viewModel.updatePermission(true)
                    }
                    else -> {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }

                }

            }

        }
        else {
            viewModel.updatePermission(true)
        }

        val stepCounterPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = {isGranted ->
                viewModel.updateStepCounterPermission(isGranted)

            }
        )

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && viewModel.hasStepCounterPermission()) {
            val context = LocalContext.current

            LaunchedEffect(stepCounterPermissionLauncher) {
                when(PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACTIVITY_RECOGNITION
                    ) -> {
                        viewModel.updateStepCounterPermission(true)
                    }
                    else -> {
                        stepCounterPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                    }
                }
            }
        }
        else {
            viewModel.updateStepCounterPermission(true)
        }

        var stepSensor = StepSensorManager()
        if(viewModel.hasStepCounterPermission() and !viewModel.isStepCounterListening()){
            var context = LocalContext.current
            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            stepCounterSensor?.let {
                sensorManager.registerListener(stepSensor, it, SensorManager.SENSOR_DELAY_FASTEST)
                viewModel.updateStepCounterListening(true)
                viewModel.setStepCounter(stepSensor)
            }

        }

        viewModel.startTimer()
        viewModel.startTracking()
        if(viewModel.hasLocationPermission() && !viewModel.isReceiverRegistered()){
            val context = LocalContext.current
            val filter = IntentFilter("com.example.projetocm.LocationBroadcast")
            val locationReceiver = LocationReceiver()
            locationReceiver.setReceiveLocation(viewModel::updatePosition)
            registerReceiver(context,locationReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
            viewModel.setIsReceiverRegistered(true)
            viewModel.setUnregisterReceiver{context.unregisterReceiver(locationReceiver)}
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
        ) {



            var uiSettings by remember { mutableStateOf(MapUiSettings(
                compassEnabled = false,
                zoomControlsEnabled = false
            )) }
            var mapProperties by remember {mutableStateOf(MapProperties(mapType = MapType.NORMAL))}

            val baseZoom = 16f
            var defaultPosition = LatLng(40.63319811102272, -8.65936701396476)
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(defaultPosition, baseZoom)
            }
            if(viewModel.isReceiverRegistered()){
                cameraPositionState.position=CameraPosition.fromLatLngZoom(viewModel.getLastPosition(), baseZoom)
            }



            GoogleMap(
                properties = mapProperties,
                uiSettings= uiSettings,
                modifier = Modifier
                    .padding(10.dp),
                cameraPositionState = cameraPositionState
            ){
                Polyline(points = viewModel.getPoints())
                Marker(
                    state= MarkerState(viewModel.getStartingPosition()),
                    title= "Start",
                    icon= BitmapFromVector(LocalContext.current,R.drawable.baseline_flag_24)
                )

                Marker(
                    state= MarkerState(viewModel.getLastPosition()),
                    title= "Current position",
                    icon= BitmapFromVector(LocalContext.current,R.drawable.baseline_directions_run_24),
                    zIndex = 3f
                )

            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
        ) {

            SessionInfo(
                modifier = Modifier
                    .fillMaxWidth(),
                sessionInfoDetails = viewModel.sessionInfoUI.sessionInfoDetails
            )

            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Button(
                    onClick = viewModel::pauseUnpauseClick,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(id = if(viewModel.sessionInfoUI.paused) R.string.r_continue else R.string.pause)
                    )
                }

                Button(
                    onClick = {
                              coroutineScope.launch {
                                  val id = viewModel.finishSession()
                                  //mandar para a pagina de final de sessao
                                  onSessionEnd(id)
                              }
                    },
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.finish)
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Button(
                    onClick = onNavigateToCamera,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.take_photo)
                    )
                }

                Button(
                    onClick = {
                        viewModel.reset()
                        onSessionCancel()
                    },
                    modifier= Modifier.weight(1f)
                ) {
                    Text(
                        text= stringResource(id = R.string.cancel_session)
                    )
                }
            }

        }
    }
}

@Composable
fun SessionInfo(
    modifier: Modifier = Modifier,
    sessionInfoDetails: SessionInfoDetails
) {
    Column(
        modifier = modifier
    ) {
        InfoRow(
            text1Label = R.string.time_label,
            text1Value = sessionInfoDetails.time,
            text1Metric = R.string.empty,
            text2Label = R.string.distance,
            text2Value = sessionInfoDetails.distance,
            text2Metric = R.string.km
        )

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        
        InfoRow(
            text1Label = R.string.top_speed,
            text1Value = sessionInfoDetails.topSpeed,
            text1Metric = R.string.km_per_hour,
            text2Label = R.string.steps_taken,
            text2Value = sessionInfoDetails.stepsTaken,
            text2Metric = R.string.empty
        )

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        
        InfoRow(
            text1Label = R.string.average_speed,
            text1Value = sessionInfoDetails.averageSpeed,
            text1Metric = R.string.km_per_hour,
            text2Label = R.string.calories_burned,
            text2Value = sessionInfoDetails.calories,
            text2Metric = R.string.cal
        )
    }
}

@Composable
fun InfoRow(
    @StringRes
    text1Label: Int,
    text1Value: String,
    @StringRes
    text1Metric: Int,
    @StringRes
    text2Label: Int,
    text2Value: String,
    @StringRes
    text2Metric: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text= stringResource(id = text1Label),
                style= MaterialTheme.typography.labelSmall
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text= text1Value,
                    style= MaterialTheme.typography.bodyLarge
                )

                Text(
                    text= stringResource(id = text1Metric),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text= stringResource(id = text2Label),
                style= MaterialTheme.typography.labelSmall
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text= text2Value,
                    style= MaterialTheme.typography.bodyLarge
                )

                Text(
                    text= stringResource(id = text2Metric),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
/*
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SessionInProgressPreview(){
    ProjetoCMTheme {
        Scaffold {
            SessionInProgress(onNavigateToCamera = {},modifier = Modifier.padding(it))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SessionInProgressPreviewDark(){
    ProjetoCMTheme(darkTheme = true) {
        Scaffold{
            SessionInProgress(onNavigateToCamera = {},modifier = Modifier.padding(it))
        }
    }
}*/