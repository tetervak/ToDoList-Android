package com.example.todolist.ui.screens.tasks

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.unit.dp
import com.example.todolist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownContextMenu(
    onTaskAction: (TaskActionOption) -> Unit,
    modifier: Modifier = Modifier,
) {
  var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        modifier = modifier,
        onExpandedChange = { isExpanded = it }
    ) {
        Icon(
            modifier = Modifier.padding(8.dp, 0.dp)
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More"
        )

        ExposedDropdownMenu(
            modifier = Modifier.width(180.dp),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            val options = stringArrayResource(R.array.task_action_options)

            options.forEachIndexed { index, selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        isExpanded = false
                        onTaskAction(TaskActionOption.entries[index])
                    },
                    text = { Text(text = selectionOption) }
                )
            }
        }
    }
}