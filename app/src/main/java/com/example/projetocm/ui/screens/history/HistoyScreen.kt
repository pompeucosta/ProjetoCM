package com.example.projetocm.ui.screens.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetocm.R
import com.example.projetocm.data.HistorySession
import com.example.projetocm.data.Session
import com.example.projetocm.data.sessions
import com.example.projetocm.ui.AppViewModelProvider
import com.example.projetocm.ui.theme.ProjetoCMTheme


@Composable
fun History(
    modifier: Modifier = Modifier,
    onSessionClick: (Int) -> Unit = {},
    viewModel: HistoryViewModel = viewModel(factory= AppViewModelProvider.Factory)
) {
    val sessions by viewModel.savedSessionsUIState.collectAsState()
    val sessionList = sessions.sessionList

    if(sessionList.isEmpty()) {

    }
    else {
        LazyColumn(modifier= modifier) {
            items(sessionList) { session ->
                SessionItem(
                    session = session.toUIInfo(),
                    modifier= Modifier
                        .clickable{onSessionClick(session.id)}
                )
            }
        }
    }

}

@Composable
fun SessionItem(
    session: HistorySessionUIInfo,
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
                    text= session.sessionDetails.distance,
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
                    text= session.date, //"${session.date.dayOfMonth}/${session.date.month}/${session.date.year}"
                    style= MaterialTheme.typography.bodyLarge
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = session.sessionDetails.time,
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