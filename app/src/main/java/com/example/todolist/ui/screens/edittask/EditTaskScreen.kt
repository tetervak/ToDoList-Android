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

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.R
import com.example.todolist.R.drawable as AppIcon
import com.example.todolist.R.string as AppText
import com.example.todolist.ui.common.ext.card
import com.example.todolist.ui.common.ext.fieldModifier
import com.example.todolist.ui.common.ext.spacer
import com.example.todolist.data.Priority
import com.example.todolist.data.ToDoTask
import com.example.todolist.ui.common.composable.BasicField
import com.example.todolist.ui.theme.ToDoListTheme

@Composable
@ExperimentalMaterial3Api
fun EditTaskScreen(
    popUpScreen: () -> Unit,
    viewModel: EditTaskViewModel = hiltViewModel()
) {
    val task by viewModel.toDoTask

    EditTaskScreenContent(
        toDoTask = task,
        onDoneClick = { viewModel.onDoneClick(popUpScreen) },
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onUrlChange = viewModel::onUrlChange,
        onDateChange = viewModel::onDateChange,
        onTimeChange = viewModel::onTimeChange,
        onPriorityChange = viewModel::onPriorityChange,
        onFlagToggle = viewModel::onFlagToggle
    )
}

@Composable
@ExperimentalMaterial3Api
fun EditTaskScreenContent(
    modifier: Modifier = Modifier,
    toDoTask: ToDoTask,
    onDoneClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onUrlChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onTimeChange: (Int, Int) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onFlagToggle: (Boolean) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(text = stringResource(AppText.edit_task))
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            actions = {
                IconButton(
                    onClick = onDoneClick
                ) {
                    Icon(
                        painter = painterResource(AppIcon.ic_check),
                        contentDescription = stringResource(AppText.ok)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.spacer())

        val fieldModifier = Modifier.fieldModifier()
        BasicField(AppText.title, toDoTask.title, onTitleChange, fieldModifier)
        BasicField(AppText.description, toDoTask.description, onDescriptionChange, fieldModifier)
        BasicField(AppText.url, toDoTask.url, onUrlChange, fieldModifier)

        Spacer(modifier = Modifier.spacer())

        DateInput(displayedDate = toDoTask.dueDate, onDateChange)

        TimeInput(displayTime = toDoTask.dueTime, onTimeChange)

        PriorityInput(Priority.valueOf(toDoTask.priority), onPriorityChange)

        FlagSelector(AppText.flag, toDoTask.flag, Modifier.card()) { newValue ->
            onFlagToggle(newValue)
        }

        Spacer(modifier = Modifier.spacer())
    }
}

@Preview(showBackground = true)
@ExperimentalMaterial3Api
@Composable
fun EditTaskScreenPreview() {
    val toDoTask = ToDoTask(
        title = "Task title",
        description = "Task description",
        flag = true
    )

    ToDoListTheme {
        EditTaskScreenContent(
            toDoTask = toDoTask,
            onDoneClick = { },
            onTitleChange = { },
            onDescriptionChange = { },
            onUrlChange = { },
            onDateChange = { },
            onTimeChange = { _, _ -> },
            onPriorityChange = { },
            onFlagToggle = { }
        )
    }
}

@Composable
@ExperimentalMaterial3Api
private fun FlagSelector(
    @StringRes label: Int,
    flag: Boolean,
    modifier: Modifier,
    onNewValue: (Boolean) -> Unit
) {
    OutlinedCard(
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        ),
        modifier = modifier
    ) {
        var isExpanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            modifier = Modifier,
            onExpandedChange = { isExpanded = it }
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
                readOnly = true,
                value = if (flag) stringResource(R.string.flag_on) else stringResource(R.string.flag_off),
                onValueChange = {},
                label = { Text(stringResource(label)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded) },
                colors = dropdownColors()
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                DropdownMenuItem(
                    onClick = {
                        onNewValue(true)
                        isExpanded = false
                    },
                    text = { Text(text = stringResource(R.string.flag_on)) }
                )
                DropdownMenuItem(
                    onClick = {
                        onNewValue(false)
                        isExpanded = false
                    },
                    text = { Text(text = stringResource(R.string.flag_off)) }
                )
            }
        }
    }
}

