package com.example.todolist.ui.screens.edittask

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import com.example.todolist.R
import com.example.todolist.data.Priority
import com.example.todolist.ui.common.ext.card

@ExperimentalMaterial3Api
@Composable
fun PriorityInput(
    priority: Priority,
    onPriorityChange: (Priority) -> Unit
) {
    val options = stringArrayResource(R.array.task_priority_options)

    OutlinedCard(
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        ),
        modifier = Modifier.card()
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
                value = options[priority.ordinal],
                onValueChange = {},
                label = { Text(stringResource(R.string.priority)) },
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
                            onPriorityChange(Priority.entries[index])
                            isExpanded = false
                        },
                        text = { Text(text = selectionOption) }
                    )
                }
            }
        }
    }
}