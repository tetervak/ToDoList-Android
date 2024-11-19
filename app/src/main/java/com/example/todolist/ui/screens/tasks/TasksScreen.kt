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

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todolist.R.drawable as AppIcon
import com.example.todolist.R.string as AppText
import com.example.todolist.data.Task
import com.example.todolist.ui.theme.ToDoListTheme

@Composable
@ExperimentalMaterial3Api
fun TasksScreen(
    onAddTask: () -> Unit,
    onEditTask: (String) -> Unit,
    onStats: () -> Unit,
    onSettings: () -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val tasks: State<List<Task>> = viewModel.tasks.collectAsStateWithLifecycle(emptyList())

    TasksScreenContent(
        tasks = tasks.value,
        onAddTask = onAddTask,
        onCheckChange = viewModel::onTaskCheckChange,
        onEditTask = onEditTask,
        onToggleFlag = viewModel::onToggleFlag,
        onDeleteTask = viewModel::onDeleteTask,
        onStats = onStats,
        onSettings = onSettings,
    )
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
fun TasksScreenContent(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    onAddTask: () -> Unit,
    onCheckChange: (Task) -> Unit,
    onEditTask: (String) -> Unit,
    onToggleFlag: (Task) -> Unit,
    onDeleteTask: (String) -> Unit,
    onStats: () -> Unit,
    onSettings: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(AppText.tasks))
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(
                        onClick = onStats
                    ) {
                        Icon(
                            painter = painterResource(AppIcon.ic_stats),
                            contentDescription = stringResource(AppText.stats)
                        )
                    }
                    IconButton(
                        onClick = onSettings
                    ) {
                        Icon(
                            painter = painterResource(AppIcon.ic_settings),
                            contentDescription = stringResource(AppText.settings)
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTask,
                modifier = modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(tasks, key = { it.id }) { taskItem ->
                TaskItem(
                    task = taskItem,
                    onCheckChange = { onCheckChange(taskItem) },
                    onTaskAction = { option: TaskActionOption ->
                        when (option) {
                            TaskActionOption.EDIT_TASK -> onEditTask(taskItem.id)
                            TaskActionOption.TOGGLE_FLAG -> onToggleFlag(taskItem)
                            TaskActionOption.DELETE_TASK -> onDeleteTask(taskItem.id)
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@ExperimentalMaterial3Api
@Composable
fun TasksScreenPreview() {
    ToDoListTheme {
        TasksScreenContent(
            tasks = listOf(
                Task(
                    title = "Test",
                    completed = true,
                    flag = true
                )
            ),
            onAddTask = {},
            onEditTask = {},
            onToggleFlag = {},
            onDeleteTask = {},
            onCheckChange = {},
            onStats = {},
            onSettings = {}
        )
    }
}
