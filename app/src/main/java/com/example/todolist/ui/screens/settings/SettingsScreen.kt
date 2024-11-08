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

package com.example.todolist.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.R.drawable as AppIcon
import com.example.todolist.R.string as AppText
import com.example.todolist.ui.common.ext.card
import com.example.todolist.ui.common.ext.spacer
import com.example.todolist.ui.common.composable.BasicToolbar
import com.example.todolist.ui.common.composable.DangerousCardEditor
import com.example.todolist.ui.common.composable.DialogCancelButton
import com.example.todolist.ui.common.composable.DialogConfirmButton
import com.example.todolist.ui.common.composable.RegularCardEditor
import com.example.todolist.ui.theme.ToDoListTheme

@ExperimentalMaterial3Api
@Composable
fun SettingsScreen(
  restartApp: () -> Unit,
  onLogin: () -> Unit,
  onSignUp: () -> Unit,
  viewModel: SettingsViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsState(initial = SettingsUiState(false))

  SettingsScreenContent(
    uiState = uiState,
    onLogin = onLogin,
    onSignUp = onSignUp,
    onSignOut = { viewModel.onSignOut(restartApp) },
    onDeleteMyAccount = { viewModel.onDeleteMyAccount(restartApp) }
  )
}

@ExperimentalMaterial3Api
@Composable
fun SettingsScreenContent(
  modifier: Modifier = Modifier,
  uiState: SettingsUiState,
  onLogin: () -> Unit,
  onSignUp: () -> Unit,
  onSignOut: () -> Unit,
  onDeleteMyAccount: () -> Unit
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    BasicToolbar(AppText.settings)

    Spacer(modifier = Modifier.spacer())

    if (uiState.isAnonymousAccount) {
      RegularCardEditor(AppText.sign_in, AppIcon.ic_sign_in, "", Modifier.card()) {
        onLogin()
      }

      RegularCardEditor(AppText.create_account, AppIcon.ic_create_account, "", Modifier.card()) {
        onSignUp()
      }
    } else {
      SignOutCard { onSignOut() }
      DeleteMyAccountCard { onDeleteMyAccount() }
    }
  }
}

@ExperimentalMaterial3Api
@Composable
private fun SignOutCard(signOut: () -> Unit) {
  var showWarningDialog by remember { mutableStateOf(false) }

  RegularCardEditor(AppText.sign_out, AppIcon.ic_exit, "", Modifier.card()) {
    showWarningDialog = true
  }

  if (showWarningDialog) {
    AlertDialog(
      title = { Text(stringResource(AppText.sign_out_title)) },
      text = { Text(stringResource(AppText.sign_out_description)) },
      dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
      confirmButton = {
        DialogConfirmButton(AppText.sign_out) {
          signOut()
          showWarningDialog = false
        }
      },
      onDismissRequest = { showWarningDialog = false }
    )
  }
}

@ExperimentalMaterial3Api
@Composable
private fun DeleteMyAccountCard(deleteMyAccount: () -> Unit) {
  var showWarningDialog by remember { mutableStateOf(false) }

  DangerousCardEditor(
    AppText.delete_my_account,
    AppIcon.ic_delete_my_account,
    "",
    Modifier.card()
  ) {
    showWarningDialog = true
  }

  if (showWarningDialog) {
    AlertDialog(
      title = { Text(stringResource(AppText.delete_account_title)) },
      text = { Text(stringResource(AppText.delete_account_description)) },
      dismissButton = { DialogCancelButton(AppText.cancel) { showWarningDialog = false } },
      confirmButton = {
        DialogConfirmButton(AppText.delete_my_account) {
          deleteMyAccount()
          showWarningDialog = false
        }
      },
      onDismissRequest = { showWarningDialog = false }
    )
  }
}

@Preview(showBackground = true)
@ExperimentalMaterial3Api
@Composable
fun SettingsScreenPreview() {
  val uiState = SettingsUiState(isAnonymousAccount = false)

  ToDoListTheme {
    SettingsScreenContent(
      uiState = uiState,
      onLogin = { },
      onSignUp = { },
      onSignOut = { },
      onDeleteMyAccount = { }
    )
  }
}
