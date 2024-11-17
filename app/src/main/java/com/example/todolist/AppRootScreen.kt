/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.todolist

import android.Manifest
import android.content.res.Resources
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.todolist.ui.common.composable.PermissionDialog
import com.example.todolist.ui.common.composable.RationaleDialog
import com.example.todolist.ui.common.snackbar.SnackbarManager
import com.example.todolist.ui.navigation.ToDoListNavHost
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.CoroutineScope

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppRootScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      RequestNotificationPermissionDialog()
    }

    Surface(color = MaterialTheme.colorScheme.background) {

      val appState: ToDoListAppState = rememberAppState()

      Scaffold(
        snackbarHost = {
          SnackbarHost(
            hostState = appState.snackbarHostState,
            modifier = Modifier.padding(8.dp),
            snackbar = { snackbarData ->
              Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.onPrimary)
            }
          )
        },
      ) { innerPadding ->
        ToDoListNavHost(appState, Modifier.padding(innerPadding))
      }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RequestNotificationPermissionDialog() {
  val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

  if (!permissionState.status.isGranted) {
    if (permissionState.status.shouldShowRationale) RationaleDialog()
    else PermissionDialog { permissionState.launchPermissionRequest() }
  }
}

@Composable
fun rememberAppState(
  snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
  navController: NavHostController = rememberNavController(),
  snackbarManager: SnackbarManager = SnackbarManager,
  resources: Resources = resources(),
  coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
  remember(snackbarHostState, navController, snackbarManager, resources, coroutineScope) {
    ToDoListAppState(snackbarHostState, navController, snackbarManager, resources, coroutineScope)
  }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
  //LocalConfiguration.current
  return LocalContext.current.resources
}
