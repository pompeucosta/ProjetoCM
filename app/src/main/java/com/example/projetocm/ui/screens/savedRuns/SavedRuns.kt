package com.example.projetocm.ui.screens.savedRuns

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetocm.R
import com.example.projetocm.data.RunPreset
import com.example.projetocm.data.RunPresets
import com.example.projetocm.ui.theme.ProjetoCMTheme


@Composable
fun SavedRuns(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(RunPresets) {preset ->
            Preset(
                preset = preset,
                modifier= Modifier
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun Preset(preset: RunPreset,modifier: Modifier = Modifier) {
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
                        text= "${preset.time.hours}:${preset.time.minutes}:${preset.time.seconds}",
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
                    text = stringResource(id = R.string.one_way),
                    style = MaterialTheme.typography.displayMedium
                )

                Switch(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End),
                    checked = preset.twoWay,
                    onCheckedChange = {}
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SavedRunsPreview() {
    ProjetoCMTheme(darkTheme = false) {
        SavedRuns()
    }
}

@Preview(showBackground = true)
@Composable
fun SavedRunsPreviewDark() {
    ProjetoCMTheme(darkTheme = true) {
        SavedRuns()
    }
}