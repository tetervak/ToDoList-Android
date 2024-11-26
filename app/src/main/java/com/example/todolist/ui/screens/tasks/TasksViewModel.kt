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

package com.example.todolist.ui.screens.tasks

import com.example.todolist.data.ToDoTask
import com.example.todolist.data.service.LogService
import com.example.todolist.data.service.StorageService
import com.example.todolist.ui.screens.ToDoListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
  logService: LogService,
  private val storageService: StorageService
) : ToDoListViewModel(logService) {

  val tasks: Flow<List<ToDoTask>> = storageService.tasks

  fun onTaskCheckChange(toDoTask: ToDoTask) {
    launchCatching { storageService.update(toDoTask.copy(completed = !toDoTask.completed)) }
  }

  fun onToggleFlag(toDoTask: ToDoTask) {
    launchCatching { storageService.update(toDoTask.copy(flag = !toDoTask.flag)) }
  }

  fun onDeleteTask(id: String) {
    launchCatching { storageService.delete(id) }
  }
}
