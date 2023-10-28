@file:OptIn(ExperimentalPermissionsApi::class)

package com.example.projetocm.ui.screens.camera

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.example.projetocm.ui.screens.camera.CameraPreviewScreen
import com.example.projetocm.ui.screens.camera.NoCameraPermissionScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainCameraScreen() {

    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    MainCameraContent(
        hasPermission = cameraPermissionState.status.isGranted,
        onRequestPermission = cameraPermissionState::launchPermissionRequest
    )
}

@Composable
private fun MainCameraContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit
) {

    if (hasPermission) {
        CameraPreviewScreen()
    } else {
        NoCameraPermissionScreen(onRequestPermission)
    }
}

@Preview
@Composable
private fun Preview_MainContent() {
    MainCameraContent(
        hasPermission = true,
        onRequestPermission = {}
    )
}