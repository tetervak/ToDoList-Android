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

package com.example.todolist.ui.screens.edittask

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.example.todolist.ui.navigation.TASK_ID
import com.example.todolist.ui.common.ext.idFromParameter
import com.example.todolist.data.Task
import com.example.todolist.data.service.LogService
import com.example.todolist.data.service.StorageService
import com.example.todolist.ui.screens.MakeItSoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  logService: LogService,
  private val storageService: StorageService,
) : MakeItSoViewModel(logService) {
  val task = mutableStateOf(Task())

  init {
    val taskId = savedStateHandle.get<String>(TASK_ID)
    if (taskId != null) {
      launchCatching {
        task.value = storageService.getTask(taskId.idFromParameter()) ?: Task()
      }
    }
  }

  fun onTitleChange(newValue: String) {
    task.value = task.value.copy(title = newValue)
  }

  fun onDescriptionChange(newValue: String) {
    task.value = task.value.copy(description = newValue)
  }

  fun onUrlChange(newValue: String) {
    task.value = task.value.copy(url = newValue)
  }

  fun onDateChange(newValue: Long) {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone(UTC))
    calendar.timeInMillis = newValue
    val offset = TimeZone.getDefault().getOffset(calendar.timeInMillis)
    calendar.add(Calendar.MILLISECOND, -offset)
    calendar.timeZone = TimeZone.getDefault()
    val newDueDate = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(calendar.time)
    task.value = task.value.copy(dueDate = newDueDate)
  }

  fun onTimeChange(hour: Int, minute: Int) {
    val newDueTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
    task.value = task.value.copy(dueTime = newDueTime)
  }

  fun onFlagToggle(newValue: String) {
    val newFlagOption = EditFlagOption.getBooleanValue(newValue)
    task.value = task.value.copy(flag = newFlagOption)
  }

  fun onPriorityChange(newValue: String) {
    task.value = task.value.copy(priority = newValue)
  }

  fun onDoneClick(popUpScreen: () -> Unit) {
    launchCatching {
      val editedTask = task.value
      if (editedTask.id.isBlank()) {
        storageService.save(editedTask)
      } else {
        storageService.update(editedTask)
      }
      popUpScreen()
    }
  }

  companion object {
    private const val UTC = "UTC"
    private const val DATE_FORMAT = "EEE, d MMM yyyy"
  }
}
