package com.example.projetocm.ui.screens.session

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetocm.R
import com.example.projetocm.ui.theme.ProjetoCMTheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings

@Composable
fun SessionInProgress(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
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

            GoogleMap(
                properties = mapProperties,
                uiSettings= uiSettings,
                modifier = Modifier
                    .padding(10.dp)
            )
        }

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
        ) {

            SessionInfo(
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(id = R.string.pause)
                    )
                }

                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                )

                Button(
                    onClick = {},
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
                onClick = {},
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
fun SessionInfo(modifier: Modifier = Modifier) {
    val time = 120
    val distance = 4.8
    val topSpeed = 9.4
    val stepsTaken = 1568
    val averageSpeed = 5.3
    val calories = 593

    Column(
        modifier = modifier
    ) {
        InfoRow(
            text1Label = R.string.time_label,
            text1Value = time.toString(),
            text1Metric = R.string.minutes,
            text2Label = R.string.distance,
            text2Value = distance.toString(),
            text2Metric = R.string.km
        )

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        
        InfoRow(
            text1Label = R.string.top_speed,
            text1Value = topSpeed.toString(),
            text1Metric = R.string.km_per_hour,
            text2Label = R.string.steps_taken,
            text2Value = stepsTaken.toString(),
            text2Metric = R.string.steps_taken
        )

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )
        
        InfoRow(
            text1Label = R.string.average_speed,
            text1Value = averageSpeed.toString(),
            text1Metric = R.string.km_per_hour,
            text2Label = R.string.calories_burned,
            text2Value = calories.toString(),
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
                verticalAlignment = Alignment.CenterVertically
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
                verticalAlignment = Alignment.CenterVertically
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
        Scaffold(

        ) {
            SessionInProgress(modifier = Modifier.padding(it))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SessionInProgressPreviewDark(){
    ProjetoCMTheme(darkTheme = true) {
        Scaffold(

        ) {
            SessionInProgress(modifier = Modifier.padding(it))
        }
    }
}