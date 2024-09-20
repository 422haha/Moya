package com.ssafy.ar

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun MultiplePermissionsHandler(
    permissions: List<String>,
    onPermissionsResult: (Map<String, Boolean>) -> Unit,
) {
    val context = LocalContext.current

    val permissionsToRequest =
        remember(permissions) {
            permissions.filter {
                ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
            }
        }

    val multiplePermissionResultLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { perms ->
                onPermissionsResult(perms)
            },
        )

    LaunchedEffect(permissions) {
        if (permissionsToRequest.isNotEmpty()) {
            multiplePermissionResultLauncher.launch(permissionsToRequest.toTypedArray())
        } else {
            // All permissions are already granted
            onPermissionsResult(permissions.associateWith { true })
        }
    }
}