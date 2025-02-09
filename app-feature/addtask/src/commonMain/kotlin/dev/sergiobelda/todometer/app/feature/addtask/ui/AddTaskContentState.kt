/*
 * Copyright 2025 Sergio Belda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.sergiobelda.todometer.app.feature.addtask.ui

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.sergiobelda.todometer.app.common.ui.extensions.selectedTimeMillis
import dev.sergiobelda.todometer.common.domain.model.Tag
import dev.sergiobelda.todometer.common.ui.base.BaseContentState
import dev.sergiobelda.todometer.common.ui.base.BaseEvent

@OptIn(ExperimentalMaterial3Api::class)
data class AddTaskContentState internal constructor(
    val snackbarHostState: SnackbarHostState,
    val topAppBarState: TopAppBarState,
    val datePickerState: DatePickerState,
    val timePickerState: TimePickerState,
) : BaseContentState {
    private val tags = enumValues<Tag>()

    // SAVE
    var taskTitle by mutableStateOf("")
        private set

    // SAVE
    var taskDescription by mutableStateOf("")
        private set

    // SAVE
    var selectedTag by mutableStateOf(tags.firstOrNull() ?: Tag.UNSPECIFIED)

    // SAVE
    var taskDueDate: Long? by mutableStateOf(null)

    // SAVE
    val taskChecklistItems = mutableStateListOf<String>()

    var discardTaskAlertDialogVisible by mutableStateOf(false)
        private set

    var datePickerDialogVisible by mutableStateOf(false)
        private set

    var timePickerDialogVisible by mutableStateOf(false)
        private set

    suspend fun showSnackbar(message: String) =
        snackbarHostState.showSnackbar(message = message)

    override fun handleEvent(event: BaseEvent) {
        when (event) {
            is AddTaskEvent.OnConfirmDatePickerDialog -> confirmDatePickerDialog()
            is AddTaskEvent.OnDismissDatePickerDialog -> dismissDatePickerDialog()
            is AddTaskEvent.OnConfirmTimePickerDialog -> confirmTimePickerDialog()
            is AddTaskEvent.OnDismissTimePickerDialog -> dismissDatePickerDialog()
            is AddTaskEvent.OnDismissDiscardTaskDialog -> dismissDiscardTaskDialog()
            is AddTaskEvent.OnBack -> checkOnBack(event)
            is AddTaskEvent.TaskTitleValueChange -> taskTitleValueChange(event)
        }
    }

    private fun confirmDatePickerDialog() {
        datePickerDialogVisible = false
        updateTaskDueDate()
    }

    private fun dismissDatePickerDialog() {
        datePickerDialogVisible = false
    }

    private fun confirmTimePickerDialog() {
        timePickerDialogVisible = false
        updateTaskDueDate()
    }

    private fun dismissTimePickerDialog() {
        timePickerDialogVisible = false
    }

    private fun updateTaskDueDate() {
        taskDueDate = datePickerState.selectedDateMillis?.plus(timePickerState.selectedTimeMillis)
    }

    private fun dismissDiscardTaskDialog() {
        discardTaskAlertDialogVisible = false
    }

    private fun checkOnBack(event: AddTaskEvent.OnBack) {
        if (initialValuesUpdated()) {
            discardTaskAlertDialogVisible = true
        } else {
            event.navigateBack.invoke()
        }
    }

    private fun initialValuesUpdated(): Boolean =
        taskTitle.isNotBlank() ||
            taskDueDate != null ||
            taskDescription.isNotBlank() ||
            taskChecklistItems.isNotEmpty()

    private fun taskTitleValueChange(event: AddTaskEvent.TaskTitleValueChange) {
        taskTitle = event.value
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberAddTaskContentState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    datePickerState: DatePickerState = rememberDatePickerState(),
    timePickerState: TimePickerState = rememberTimePickerState(),
): AddTaskContentState = remember {
    AddTaskContentState(
        snackbarHostState = snackbarHostState,
        topAppBarState = topAppBarState,
        datePickerState = datePickerState,
        timePickerState = timePickerState,
    )
}
