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

package com.example.todolist.ui.screens.splash

import androidx.compose.runtime.mutableStateOf
import com.example.todolist.data.service.AccountService
import com.example.todolist.data.service.ConfigurationService
import com.example.todolist.data.service.LogService
import com.example.todolist.ui.screens.MakeItSoViewModel
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
  configurationService: ConfigurationService,
  private val accountService: AccountService,
  logService: LogService
) : MakeItSoViewModel(logService) {

  val showError = mutableStateOf(false)

  init {
    launchCatching { configurationService.fetchConfiguration() }
  }

  fun onAppStart(onSplashed: () -> Unit) {
    showError.value = false
    if (accountService.hasUser){
      onSplashed()
    }
    else {
      createAnonymousAccount(onSplashed)
    }
  }

  private fun createAnonymousAccount(onSplashed: () -> Unit) {
    launchCatching(snackbar = false) {
      try {
        accountService.createAnonymousAccount()
      } catch (ex: FirebaseAuthException) {
        showError.value = true
        throw ex
      }
      onSplashed()
    }
  }
}
