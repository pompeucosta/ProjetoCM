package com.example.projetocm.ui.screens.savedRuns

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetocm.R
import com.example.projetocm.ui.AppViewModelProvider
import com.example.projetocm.ui.theme.ProjetoCMTheme
import com.example.projetocm.ui.utils.Picker
import kotlinx.coroutines.launch

@Composable
fun CreateRun(
    modifier: Modifier = Modifier,
    viewModel: CreateRunViewModel = viewModel(factory= AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text= stringResource(id = R.string.time_label),
            style= MaterialTheme.typography.displayMedium,
            modifier= Modifier
                .align(Alignment.CenterHorizontally)
        )

        TimePicker(
            modifier= Modifier
                .fillMaxWidth(),
            presetDetails = viewModel.presetUIState.presetDetails,
            onValueChange = viewModel::updateUIState
        )

        Distance(
            presetDetails = viewModel.presetUIState.presetDetails,
            onValueChange = viewModel::updateUIState,
            modifier= Modifier
                .fillMaxWidth()
        )

        OneWay(
            presetDetails = viewModel.presetUIState.presetDetails,
            onCheckedChange = viewModel::updateUIState,
            modifier= Modifier
                .fillMaxWidth()
        )

        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .weight(weight= 1f,fill = true)
        ) {
            Column{
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.savePreset()
                            //voltar para a pagina dos presets
                        }
                    },
                    enabled = viewModel.presetUIState.isEntryValid,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text= stringResource(id = R.string.save)
                    )
                }

                Button(
                    onClick = {
                              coroutineScope.launch {
                                  viewModel.savePreset()
                                  //comecar a sessao
                              }
                    },
                    enabled = viewModel.presetUIState.isEntryValid,
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
fun TimePicker(
    modifier: Modifier = Modifier,
    presetDetails: RunPresetDetails,
    onValueChange: (RunPresetDetails) -> Unit
) {
    val hours = remember { (0..23).map{it.toString()}}

    val minutes = remember {(0..59).map{it.toString()}}

    val seconds = remember{(0..59).map{it.toString()}}

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Picker(
            items= hours,
            visibleItemsCount = 3,
            onItemSelectedChange = { onValueChange(presetDetails.copy(hours= it)) },
            companionText= stringResource(id = R.string.hours),
            companionTextStyle= MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .weight(1f)
        )

        Picker(
            items= minutes,
            visibleItemsCount = 3,
            onItemSelectedChange = {onValueChange(presetDetails.copy(minutes= it))},
            companionText= stringResource(id = R.string.minutes),
            companionTextStyle= MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .weight(1f)
        )

        Picker(
            items= seconds,
            visibleItemsCount = 3,
            onItemSelectedChange = {onValueChange(presetDetails.copy(seconds= it))},
            companionText= stringResource(id = R.string.seconds),
            companionTextStyle= MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Distance(
    presetDetails: RunPresetDetails,
    modifier: Modifier = Modifier,
    onValueChange: (RunPresetDetails) -> Unit = {}
) {
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
                    value=presetDetails.km,
                    onValueChange = {onValueChange(presetDetails.copy(km= it))},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
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
fun OneWay(
    presetDetails: RunPresetDetails,
    modifier: Modifier= Modifier,
    onCheckedChange: (RunPresetDetails) -> Unit
) {
    Row(
        modifier= modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text= stringResource(id = R.string.one_way),
            style= MaterialTheme.typography.displayMedium
        )
        Switch(
            checked = presetDetails.twoWay,
            onCheckedChange= {onCheckedChange(presetDetails.copy(twoWay = it))},
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