package com.example.projetocm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projetocm.ui.screens.history.History
import com.example.projetocm.ui.screens.savedRuns.SavedRuns
import com.example.projetocm.ui.screens.session.SessionInProgress
import com.example.projetocm.ui.screens.camera.MainCameraScreen
import com.example.projetocm.ui.screens.savedRuns.CreateRun
import com.example.projetocm.ui.theme.ProjetoCMTheme

sealed class Screen(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    data object History: Screen("History","history",Icons.Filled.Home,Icons.Outlined.Home)
    data object PresetRuns: Screen("Runs","presetRuns",Icons.Filled.List,Icons.Outlined.List)
    data object RunInProgress: Screen("Run In Progress","runInProgress",Icons.Filled.List,Icons.Outlined.List)
    data object CameraPreview: Screen("Run In Progress","cameraPreview",Icons.Filled.List,Icons.Outlined.List)
    data object  CreateRunPreset: Screen("Create Preset","createPreset",Icons.Filled.List,Icons.Outlined.List)
}
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjetoCMTheme {
                val items = listOf(
                    Screen.History,
                    Screen.PresetRuns,
                    Screen.RunInProgress
                )
                var selectedItemIndex by rememberSaveable {
                    mutableIntStateOf(0)
                }

                var topBarTitle by rememberSaveable {
                    //mudar o default para o home title
                    mutableStateOf(Screen.History.title)
                }

                //https://developer.android.com/jetpack/compose/navigation#bottom-nav
                val navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(title = topBarTitle)
                        },
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index,item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                                  selectedItemIndex = index
                                            topBarTitle = item.title
                                            navController.navigate(item.route)
                                        },
                                        label = {
                                                Text(text = item.title)
                                        },
                                        icon = {
                                            Icon(
                                                imageVector = if(index == selectedItemIndex) {
                                                    item.selectedIcon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        })
                                }
                            }
                        }
                    ) {
                        NavHost(navController = navController, startDestination = Screen.History.route, Modifier.padding(it)) {
                            composable(Screen.History.route) { History()}
                            composable(Screen.PresetRuns.route) { SavedRuns(onAddBtnClick = { navController.navigate(Screen.CreateRunPreset.route)})}
                            composable(Screen.RunInProgress.route) { SessionInProgress(onNavigateToCamera = { navController.navigate(Screen.CameraPreview.route) }) }
                            composable(Screen.CameraPreview.route) { MainCameraScreen() }
                            composable(Screen.CreateRunPreset.route) { CreateRun()}
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(title: String,modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text= title,
                    style= MaterialTheme.typography.displayLarge
                )
            }
        },
        modifier = modifier
    )
}