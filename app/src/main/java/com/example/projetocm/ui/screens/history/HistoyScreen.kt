package com.example.projetocm.ui.screens.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.projetocm.R
import com.example.projetocm.data.Session
import com.example.projetocm.data.sessions
import com.example.projetocm.ui.theme.ProjetoCMTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History(modifier: Modifier = Modifier) {
    LazyColumn(modifier= modifier) {
        items(sessions) { session ->
            SessionItem(session = session)

        }
    }
}

@Composable
fun SessionItem(
    session: Session,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small))
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_small))
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text= session.location,
                    style= MaterialTheme.typography.displayMedium
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text= "${session.km} km",
                    style= MaterialTheme.typography.displayMedium
                )
            }

        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_small))
        ){
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text= "${session.date}", //"${session.date.dayOfMonth}/${session.date.month}/${session.date.year}"
                    style= MaterialTheme.typography.bodyLarge
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = session.time,
                    style= MaterialTheme.typography.labelSmall
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HistoryPreview() {
    ProjetoCMTheme(darkTheme = false) {
        History()
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryDarkPreview() {
    ProjetoCMTheme(darkTheme = true) {
        History()
    }
}