package com.example.projetocm.ui.screens.savedRuns

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetocm.R
import com.example.projetocm.ui.theme.ProjetoCMTheme
import com.example.projetocm.ui.utils.Picker
import com.example.projetocm.ui.utils.rememberPickerState

@Composable
fun CreateRun(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(
            text= stringResource(id = R.string.time_label),
            style= MaterialTheme.typography.displayMedium,
            modifier= Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        TimePicker(
            modifier= Modifier
                .fillMaxWidth()
        )

        Spacer(
            modifier = Modifier
                .height(40.dp)
        )

        Distance(
            modifier= Modifier
                .fillMaxWidth()
        )

        Spacer(
            modifier = Modifier
                .height(40.dp)
        )

        OneWay(
            modifier= Modifier
                .fillMaxWidth()
        )

        Spacer(
            modifier = Modifier
                .height(40.dp)
        )

        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .weight(weight= 1f,fill = true)
        ) {
            Column(

            ) {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text= stringResource(id = R.string.save)
                    )
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text= stringResource(id = R.string.start_running)
                    )
                }
            }
        }

    }
}

@Composable
fun TimePicker(modifier: Modifier = Modifier) {
    val hours = remember { (0..23).map{it.toString()}}
    val hoursPickerState = rememberPickerState()

    val minutes = remember {(0..59).map{it.toString()}}
    val minutesPickerState = rememberPickerState()

    val seconds = remember{(0..59).map{it.toString()}}
    val secondsPickerState = rememberPickerState()

    Row(
        modifier = modifier
    ) {
        Picker(
            items= hours,
            state= hoursPickerState,
            visibleItemsCount = 3,
            companionText= stringResource(id = R.string.hours),
            companionTextStyle= MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .weight(1f)
                .padding(dimensionResource(id = R.dimen.padding_small))
        )

        Picker(
            items= minutes,
            state= minutesPickerState,
            visibleItemsCount = 3,
            companionText= stringResource(id = R.string.minutes),
            companionTextStyle= MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .weight(1f)
                .padding(dimensionResource(id = R.dimen.padding_small))
        )

        Picker(
            items= seconds,
            state= secondsPickerState,
            visibleItemsCount = 3,
            companionText= stringResource(id = R.string.seconds),
            companionTextStyle= MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .weight(1f)
                .padding(dimensionResource(id = R.dimen.padding_small))
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Distance(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text= stringResource(id = R.string.distance),
                style= MaterialTheme.typography.displayMedium
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value="0",
                    onValueChange = {},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number),
                    modifier= Modifier
                        .padding(dimensionResource(id = R.dimen.padding_small))
                        .weight(0.75f)
                )
                Text(
                    text= stringResource(id = R.string.km),
                    modifier= Modifier
                        .weight(0.25f)
                )
            }
        }
    }
}

@Composable
fun OneWay(modifier: Modifier= Modifier) {
    Row(
        modifier= modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text= stringResource(id = R.string.one_way),
            style= MaterialTheme.typography.displayMedium
        )
        Switch(
            checked = false,
            onCheckedChange= {},
            modifier= Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CreateRunPreview() {
    ProjetoCMTheme {
        Scaffold {
            CreateRun(
                modifier= Modifier
                    .padding(it)
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CreateRunPreviewDark() {
    ProjetoCMTheme(darkTheme = true) {
        Scaffold {
            CreateRun(
                modifier= Modifier
                    .padding(it)
            )
        }
    }
}