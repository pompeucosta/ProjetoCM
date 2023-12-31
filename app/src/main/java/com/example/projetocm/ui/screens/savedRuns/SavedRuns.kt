package com.example.projetocm.ui.screens.savedRuns

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetocm.R
import com.example.projetocm.data.RunPreset
import com.example.projetocm.data.RunPresets
import com.example.projetocm.ui.AppViewModelProvider
import com.example.projetocm.ui.theme.ProjetoCMTheme
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedRuns(
    modifier: Modifier = Modifier,
    viewModel: SavedRunsViewModel = viewModel(factory= AppViewModelProvider.Factory),
    onAddBtnClick: () -> Unit = {},
    onPresetClick: (Int) -> Unit = {},
    onEditClick: (Int) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddBtnClick) {
                Icon(Icons.Default.Add,contentDescription= "Add")
            }
        },
    ) {innerPadding ->
        val savedRunsUIState by viewModel.savedRunsUIState.collectAsState()
        val presetList = savedRunsUIState.presetList
        if(presetList.isEmpty()) {
            //mostrar uma mensagem a dizer que nao tem nada
            //ou nao mostrar nada simplesmente
        }
        else {
            PresetList(
                list = presetList,
                onPresetClick = {
                    onPresetClick(it.id)
                },
                onEditClick = {
                    onEditClick(it.id)
                },
                onDeleteClick = {
                    coroutineScope.launch {
                        viewModel.delete(it)
                    }
                },
                onCheckedChange = {
                    coroutineScope.launch {
                        viewModel.save(it)
                    }
                },
                modifier= modifier.padding(innerPadding))
        }
    }

}

@Composable
fun PresetList(
    list: List<RunPreset>,
    modifier: Modifier = Modifier,
    onPresetClick: (RunPreset) -> Unit = {},
    onEditClick: (RunPreset) -> Unit = {},
    onDeleteClick: (RunPreset) -> Unit = {},
    onCheckedChange: (RunPreset) -> Unit = {}
) {
    LazyColumn(modifier = modifier) {
        items(items= list, key = {it.id}) { preset ->
            Preset(
                preset = preset.toRunPresetDetails(),
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
                    .clickable {
                        onPresetClick(preset)
                    },
                onEditClick= onEditClick,
                onDeleteClick= onDeleteClick,
                onCheckedChange= onCheckedChange
            )
        }
    }
}

@Composable
fun Preset(
    preset: RunPresetDetails,
    modifier: Modifier = Modifier,
    onEditClick: (RunPreset) -> Unit = {},
    onDeleteClick: (RunPreset) -> Unit = {},
    onCheckedChange: (RunPreset) -> Unit = {}
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier= Modifier.weight(1f)
                ) {
                    Text(
                        text= preset.name,
                        style= MaterialTheme.typography.labelSmall
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                ) {
                    Row {
                        IconButton(onClick = { onEditClick(preset.toRunPreset()) }) {
                            Icon(Icons.Filled.Edit,contentDescription = stringResource(id = R.string.edit))
                        }

                        IconButton(onClick = { onDeleteClick(preset.toRunPreset()) }) {
                            Icon(Icons.Filled.Delete, contentDescription = stringResource(id = R.string.delete))
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text= "${preset.hours}:${preset.minutes}:${preset.seconds}",
                        style= MaterialTheme.typography.displayMedium
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "${preset.km}km",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.two_way),
                    style = MaterialTheme.typography.displayMedium
                )

                Switch(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End),
                    checked = preset.twoWay,
                    onCheckedChange = { onCheckedChange(preset.copy(twoWay = it).toRunPreset())}
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SavedRunsPreview() {
    ProjetoCMTheme(darkTheme = false) {
        PresetList(list= RunPresets)
    }
}

@Preview(showBackground = true)
@Composable
fun SavedRunsPreviewDark() {
    ProjetoCMTheme(darkTheme = true) {
        PresetList(list= RunPresets)
    }
}