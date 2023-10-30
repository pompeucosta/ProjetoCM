package com.example.projetocm.ui.screens.session

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetocm.R
import com.example.projetocm.ui.AppViewModelProvider
import com.example.projetocm.ui.theme.ProjetoCMTheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.launch

@Composable
fun SessionInProgress(onNavigateToCamera: () -> Unit,
                      modifier: Modifier = Modifier,
                      viewModel: SessionInProgressViewModel = viewModel(factory= AppViewModelProvider.Factory)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        val coroutineScope = rememberCoroutineScope()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
        ) {
            var uiSettings by remember { mutableStateOf(MapUiSettings(
                compassEnabled = false,
                //zoomControlsEnabled = false
            )) }
            var mapProperties by remember {mutableStateOf(MapProperties(mapType = MapType.NORMAL))}

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
                                  viewModel.finishSession()
                                  //mandar para a pagina de final de sessao
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

            Button(
                onClick = onNavigateToCamera,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.take_photo)
                )
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
}