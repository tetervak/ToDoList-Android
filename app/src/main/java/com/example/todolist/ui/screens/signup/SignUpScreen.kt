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

package com.example.todolist.ui.screens.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.R.string as AppText
import com.example.todolist.ui.common.ext.basicButton
import com.example.todolist.ui.common.ext.fieldModifier
import com.example.todolist.ui.common.composable.BasicButton
import com.example.todolist.ui.common.composable.BasicToolbar
import com.example.todolist.ui.common.composable.EmailField
import com.example.todolist.ui.common.composable.PasswordField
import com.example.todolist.ui.common.composable.RepeatPasswordField
import com.example.todolist.ui.theme.ToDoListTheme

@Composable
fun SignUpScreen(
  onSignedUp: () -> Unit,
  viewModel: SignUpViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState

  SignUpScreenContent(
    uiState = uiState,
    onEmailChange = viewModel::onEmailChange,
    onPasswordChange = viewModel::onPasswordChange,
    onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
    onSignUpClick = { viewModel.onSignUpClick(onSignedUp) }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenContent(
  modifier: Modifier = Modifier,
  uiState: SignUpUiState,
  onEmailChange: (String) -> Unit,
  onPasswordChange: (String) -> Unit,
  onRepeatPasswordChange: (String) -> Unit,
  onSignUpClick: () -> Unit
) {
  val fieldModifier = Modifier.fieldModifier()

  CenterAlignedTopAppBar(
    title = {
      Text(text = stringResource(AppText.create_account))
    },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.primaryContainer,
      titleContentColor = MaterialTheme.colorScheme.primary,
    )
  )

  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    EmailField(uiState.email, onEmailChange, fieldModifier)
    PasswordField(uiState.password, onPasswordChange, fieldModifier)
    RepeatPasswordField(uiState.repeatPassword, onRepeatPasswordChange, fieldModifier)

    BasicButton(AppText.create_account, Modifier.basicButton()) {
      onSignUpClick()
    }
  }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
  val uiState = SignUpUiState(
    email = "email@test.com"
  )

  ToDoListTheme {
    SignUpScreenContent(
      uiState = uiState,
      onEmailChange = { },
      onPasswordChange = { },
      onRepeatPasswordChange = { },
      onSignUpClick = { }
    )
  }
}
