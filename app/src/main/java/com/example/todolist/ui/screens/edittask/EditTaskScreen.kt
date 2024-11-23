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
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolist.R
import com.example.todolist.R.drawable as AppIcon
import com.example.todolist.R.string as AppText
import com.example.todolist.ui.common.ext.card
import com.example.todolist.ui.common.ext.fieldModifier
import com.example.todolist.ui.common.ext.spacer
import com.example.todolist.ui.common.ext.toolbarActions
import com.example.todolist.data.Priority
import com.example.todolist.data.Task
import com.example.todolist.ui.common.composable.ActionToolbar
import com.example.todolist.ui.common.composable.BasicField
import com.example.todolist.ui.common.composable.RegularCardEditor
import com.example.todolist.ui.theme.ToDoListTheme
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

@Composable
@ExperimentalMaterial3Api
fun EditTaskScreen(
  popUpScreen: () -> Unit,
  viewModel: EditTaskViewModel = hiltViewModel()
) {
  val task by viewModel.task
  val activity = LocalContext.current as AppCompatActivity

  EditTaskScreenContent(
    task = task,
    onDoneClick = { viewModel.onDoneClick(popUpScreen) },
    onTitleChange = viewModel::onTitleChange,
    onDescriptionChange = viewModel::onDescriptionChange,
    onUrlChange = viewModel::onUrlChange,
    onDateChange = viewModel::onDateChange,
    onTimeChange = viewModel::onTimeChange,
    onPriorityChange = viewModel::onPriorityChange,
    onFlagToggle = viewModel::onFlagToggle,
    activity = activity
  )
}

@Composable
@ExperimentalMaterial3Api
fun EditTaskScreenContent(
  modifier: Modifier = Modifier,
  task: Task,
  onDoneClick: () -> Unit,
  onTitleChange: (String) -> Unit,
  onDescriptionChange: (String) -> Unit,
  onUrlChange: (String) -> Unit,
  onDateChange: (Long) -> Unit,
  onTimeChange: (Int, Int) -> Unit,
  onPriorityChange: (Priority) -> Unit,
  onFlagToggle: (Boolean) -> Unit,
  activity: AppCompatActivity?
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .verticalScroll(rememberScrollState()),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    ActionToolbar(
      title = AppText.edit_task,
      modifier = Modifier.toolbarActions(),
      primaryActionIcon = AppIcon.ic_check,
      primaryAction = { onDoneClick() }
    )

    Spacer(modifier = Modifier.spacer())

    val fieldModifier = Modifier.fieldModifier()
    BasicField(AppText.title, task.title, onTitleChange, fieldModifier)
    BasicField(AppText.description, task.description, onDescriptionChange, fieldModifier)
    BasicField(AppText.url, task.url, onUrlChange, fieldModifier)

    Spacer(modifier = Modifier.spacer())

    RegularCardEditor(AppText.date, AppIcon.ic_calendar, task.dueDate, Modifier.card()) {
      val picker = MaterialDatePicker.Builder.datePicker().build()
      activity?.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { timeInMillis -> onDateChange(timeInMillis) }
      }
    }

    RegularCardEditor(AppText.time, AppIcon.ic_clock, task.dueTime, Modifier.card()) {
      val picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()

      activity?.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { onTimeChange(picker.hour, picker.minute) }
      }
    }

    PrioritySelector(AppText.priority, Priority.valueOf(task.priority), Modifier.card()) { newValue ->
      onPriorityChange(newValue)
    }

    FlagSelector(AppText.flag, task.flag, Modifier.card()) { newValue ->
      onFlagToggle(newValue)
    }

    Spacer(modifier = Modifier.spacer())
  }
}

@Preview(showBackground = true)
@ExperimentalMaterial3Api
@Composable
fun EditTaskScreenPreview() {
  val task = Task(
    title = "Task title",
    description = "Task description",
    flag = true
  )

  ToDoListTheme {
    EditTaskScreenContent(
      task = task,
      onDoneClick = { },
      onTitleChange = { },
      onDescriptionChange = { },
      onUrlChange = { },
      onDateChange = { },
      onTimeChange = { _, _ -> },
      onPriorityChange = { },
      onFlagToggle = { },
      activity = null
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
        modifier = Modifier.fillMaxWidth().menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
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

@Composable
@ExperimentalMaterial3Api
private fun PrioritySelector(
  @StringRes label: Int,
  priority: Priority,
  modifier: Modifier,
  onNewValue: (Priority) -> Unit
) {

    val options = stringArrayResource(R.array.task_priority_options)

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
        modifier = Modifier.fillMaxWidth().menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
        readOnly = true,
        value = options[priority.ordinal],
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
        options.forEachIndexed { index, selectionOption ->
          DropdownMenuItem(
            onClick = {
              onNewValue(Priority.entries[index])
              isExpanded = false
            },
            text = { Text(text = selectionOption) }
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun dropdownColors(): TextFieldColors {
  return ExposedDropdownMenuDefaults.textFieldColors(
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
    focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    unfocusedLabelColor = MaterialTheme.colorScheme.primary
  )
}