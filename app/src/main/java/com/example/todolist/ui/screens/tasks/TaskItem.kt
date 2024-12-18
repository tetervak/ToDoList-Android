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

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.R.drawable as AppIcon
import com.example.todolist.ui.common.ext.contextMenu
import com.example.todolist.ui.common.ext.hasDueDate
import com.example.todolist.ui.common.ext.hasDueTime
import com.example.todolist.data.ToDoTask
import com.example.todolist.ui.common.ext.hasDueDateOrTime
import com.example.todolist.ui.theme.DarkOrange
import java.lang.StringBuilder

@Composable
@ExperimentalMaterial3Api
fun TaskItem(
  toDoTask: ToDoTask,
  onCheckChange: () -> Unit,
  onTaskAction: (TaskActionOption) -> Unit,
) {
  OutlinedCard(
    colors = CardDefaults.
      cardColors(containerColor = MaterialTheme.colorScheme.surface),
    modifier = Modifier.padding(8.dp),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(8.dp).fillMaxWidth(),
    ) {
      Checkbox(
        checked = toDoTask.completed,
        onCheckedChange = { onCheckChange() },
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp)
      )

      if(toDoTask.hasDueDateOrTime()){
        Column(modifier = Modifier.weight(1f)) {
          Text(text = toDoTask.title, style = MaterialTheme.typography.titleSmall)
          Text(text = getDueDateAndTime(toDoTask), fontSize = 12.sp)
        }
      }else{
        Text(text = toDoTask.title, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
      }

      if (toDoTask.flag) {
        Icon(
          painter = painterResource(AppIcon.ic_flag),
          tint = DarkOrange,
          contentDescription = "Flag"
        )
      }

      DropdownContextMenu(
        onTaskAction = onTaskAction,
        modifier = Modifier.contextMenu()
      )
    }
  }
}

private fun getDueDateAndTime(toDoTask: ToDoTask): String {
  val stringBuilder = StringBuilder("")

  if (toDoTask.hasDueDate()) {
    stringBuilder.append(toDoTask.dueDate)
    stringBuilder.append(" ")
  }

  if (toDoTask.hasDueTime()) {
    stringBuilder.append("at ")
    stringBuilder.append(toDoTask.dueTime)
  }

  return stringBuilder.toString()
}
