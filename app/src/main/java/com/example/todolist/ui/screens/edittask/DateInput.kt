package com.example.todolist.ui.screens.edittask

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.todolist.R
import com.example.todolist.ui.common.composable.RegularCardEditor
import com.example.todolist.ui.common.ext.card

@ExperimentalMaterial3Api
@Composable
fun DateInput(
    displayedDate: String,
    onDateChange: (Long) -> Unit,

) {
    var showDateDialog by rememberSaveable { mutableStateOf(false) }

    if (showDateDialog) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = {
                showDateDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onDateChange(it)
                        }
                        showDateDialog = false
                    }
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDateDialog = false
                    }
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

    RegularCardEditor(R.string.date, R.drawable.ic_calendar, displayedDate, Modifier.card()) {
        showDateDialog = true
    }
}