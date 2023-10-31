package com.example.projetocm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
    val canNavigateBack: Boolean = false
) {
    data object History: Screen("History","history",Icons.Filled.Home,Icons.Outlined.Home)
    data object PresetRuns: Screen("Runs","presetRuns",Icons.Filled.List,Icons.Outlined.List)
    data object RunInProgress: Screen("Run In Progress","runInProgress",Icons.Filled.List,Icons.Outlined.List)
    data object CameraPreview: Screen("Run In Progress","cameraPreview",Icons.Filled.List,Icons.Outlined.List)
    data object  CreateRunPreset: Screen("Create Preset","createPreset",Icons.Filled.List,Icons.Outlined.List,true)
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

                var canNavigateBack by rememberSaveable {
                    mutableStateOf(false)
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
                            TopAppBar(title = topBarTitle,canNavigateBack = canNavigateBack, navigateBackClick = {navController.popBackStack()}, modifier = Modifier.fillMaxWidth())
                        },
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index,item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                                  selectedItemIndex = index
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
                            composable(Screen.History.route) {
                                History()
                                topBarTitle = Screen.History.title
                                canNavigateBack = Screen.History.canNavigateBack
                            }
                            composable(Screen.PresetRuns.route) {
                                SavedRuns(onAddBtnClick = { navController.navigate("${Screen.CreateRunPreset.route}/-1")},
                                    onEditClick = {id -> navController.navigate("createPreset/$id")},
                                    onPresetClick = {id -> navController.navigate(Screen.RunInProgress.route)})
                                topBarTitle = Screen.PresetRuns.title
                                canNavigateBack = Screen.PresetRuns.canNavigateBack
                            }
                            composable(Screen.RunInProgress.route) {
                                SessionInProgress(onNavigateToCamera = { navController.navigate(Screen.CameraPreview.route) })
                                topBarTitle = Screen.RunInProgress.title
                                canNavigateBack = Screen.RunInProgress.canNavigateBack
                            }
                            composable(Screen.CameraPreview.route) {
                                MainCameraScreen()
                                canNavigateBack = Screen.CameraPreview.canNavigateBack
                                topBarTitle = ""
                            }
                            composable("${Screen.CreateRunPreset.route}/{id}",
                                arguments = listOf(navArgument("id") {type= NavType.IntType})
                            ) {
                                CreateRun(navigateToPresets = {navController.navigate(Screen.PresetRuns.route)})
                                topBarTitle = Screen.CreateRunPreset.title
                                canNavigateBack = Screen.CreateRunPreset.canNavigateBack
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateBackClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text= title,
                    style= MaterialTheme.typography.displayLarge,
                )
            }
        },
        navigationIcon = {
            if(canNavigateBack)
                IconButton(onClick = navigateBackClick) {
                    Icon(Icons.Filled.ArrowBack,contentDescription = stringResource(id = R.string.back))
                }
        },
        modifier = modifier
    )
}