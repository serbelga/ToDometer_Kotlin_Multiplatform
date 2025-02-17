/*
 * Copyright 2023 Sergio Belda
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

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.sergiobelda.navigation.compose.extended.annotation.NavDestination
import dev.sergiobelda.todometer.app.common.designsystem.components.TodometerDivider
import dev.sergiobelda.todometer.app.common.designsystem.components.TodometerTitledTextField
import dev.sergiobelda.todometer.app.common.designsystem.theme.Alpha.applyMediumEmphasisAlpha
import dev.sergiobelda.todometer.app.common.ui.actions.SystemBackHandler
import dev.sergiobelda.todometer.app.common.ui.components.AddChecklistItemField
import dev.sergiobelda.todometer.app.common.ui.components.DatePickerDialog
import dev.sergiobelda.todometer.app.common.ui.components.DateTimeSelector
import dev.sergiobelda.todometer.app.common.ui.components.SaveActionTopAppBar
import dev.sergiobelda.todometer.app.common.ui.components.TagSelector
import dev.sergiobelda.todometer.app.common.ui.components.TimePickerDialog
import dev.sergiobelda.todometer.app.common.ui.extensions.addStyledOptionalSuffix
import dev.sergiobelda.todometer.app.common.ui.extensions.selectedTimeMillis
import dev.sergiobelda.todometer.app.common.ui.values.SectionPadding
import dev.sergiobelda.todometer.app.common.ui.values.TextFieldPadding
import dev.sergiobelda.todometer.common.designsystem.resources.images.Images
import dev.sergiobelda.todometer.common.designsystem.resources.images.icons.Close
import dev.sergiobelda.todometer.common.domain.model.Tag
import dev.sergiobelda.todometer.common.resources.TodometerResources

// TODO: Resolve LongMethod issue.
@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@NavDestination(
    name = "AddTask",
    destinationId = "addtask",
    deepLinkUris = ["app://open.add.task"],
)
@Composable
fun AddTaskScreen(
    navigateBack: () -> Unit,
    viewModel: AddTaskViewModel,
) {
    val lazyListState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)

    var discardTaskAlertDialogState by remember { mutableStateOf(false) }

    var datePickerDialogState by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var timePickerDialogState by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState()

    var taskTitle by rememberSaveable { mutableStateOf("") }
    var taskTitleInputError by remember { mutableStateOf(false) }
    var taskDescription by rememberSaveable { mutableStateOf("") }
    val tags = enumValues<Tag>()
    var selectedTag by rememberSaveable { mutableStateOf(tags.firstOrNull() ?: Tag.UNSPECIFIED) }
    var taskDueDate: Long? by rememberSaveable { mutableStateOf(null) }
    val taskChecklistItems = mutableStateListOf<String>()

    val onBack: () -> Unit = {
        if (
            initialValuesUpdated(
                taskTitle = taskTitle,
                taskDueDate = taskDueDate,
                taskDescription = taskDescription,
                taskChecklistItems = taskChecklistItems,
            )
        ) {
            discardTaskAlertDialogState = true
        } else {
            navigateBack()
        }
    }
    SystemBackHandler(onBack = onBack)

    if (viewModel.state.errorUi != null) {
        LaunchedEffect(snackbarHostState) {
            snackbarHostState.showSnackbar(
                message = viewModel.state.errorUi?.message ?: "",
            )
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SaveActionTopAppBar(
                navigateBack = onBack,
                title = TodometerResources.strings.addTask,
                isSaveButtonEnabled = !viewModel.state.isAddingTask,
                onSaveButtonClick = {
                    if (taskTitle.isBlank()) {
                        taskTitleInputError = true
                    } else {
                        viewModel.insertTask(
                            taskTitle,
                            selectedTag,
                            taskDescription,
                            taskDueDate,
                            taskChecklistItems,
                        )
                        navigateBack()
                    }
                },
                saveButtonTintColor = if (viewModel.state.isAddingTask) {
                    MaterialTheme.colorScheme.onSurface.applyMediumEmphasisAlpha()
                } else {
                    MaterialTheme.colorScheme.primary
                },
            )
        },
        content = { paddingValues ->
            if (viewModel.state.isAddingTask) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            LazyColumn(state = lazyListState, modifier = Modifier.padding(paddingValues)) {
                item {
                    TodometerTitledTextField(
                        title = TodometerResources.strings.name,
                        value = taskTitle,
                        onValueChange = {
                            taskTitle = it
                            taskTitleInputError = false
                        },
                        placeholder = { Text(TodometerResources.strings.enterTaskName) },
                        isError = taskTitleInputError,
                        errorMessage = TodometerResources.strings.fieldNotEmpty,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next,
                        ),
                        modifier = Modifier.padding(TextFieldPadding),
                    )
                }
                item {
                    FieldTitle(text = TodometerResources.strings.chooseTag)
                    TagSelector(selectedTag) { tag ->
                        selectedTag = tag
                    }
                }
                item {
                    FieldTitle(text = TodometerResources.strings.dateTime.addStyledOptionalSuffix())
                    DateTimeSelector(
                        taskDueDate,
                        onEnterDateTimeClick = { datePickerDialogState = true },
                        onDateClick = { datePickerDialogState = true },
                        onTimeClick = { timePickerDialogState = true },
                        onClearDateTimeClick = { taskDueDate = null },
                    )
                }
                item {
                    Text(
                        text = TodometerResources.strings.checklist.addStyledOptionalSuffix(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(
                            start = SectionPadding,
                            top = 16.dp,
                            end = SectionPadding,
                        ),
                    )
                }
                itemsIndexed(taskChecklistItems) { index, item ->
                    TaskChecklistItem(
                        item,
                        onDeleteTaskCheckListItem = { taskChecklistItems.removeAt(index) },
                    )
                }
                item {
                    AddChecklistItemField(
                        placeholder = { Text(TodometerResources.strings.addElementOptional) },
                        onAddTaskCheckListItem = { taskChecklistItems.add(it) },
                    )
                }
                item {
                    TodometerTitledTextField(
                        title = TodometerResources.strings.description.addStyledOptionalSuffix(),
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
                        placeholder = { Text(TodometerResources.strings.enterDescription) },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Done,
                        ),
                        modifier = Modifier.padding(TextFieldPadding),
                        maxLines = 4,
                    )
                }
                item {
                    TodometerDivider()
                }
            }
            if (discardTaskAlertDialogState) {
                DiscardTaskAlertDialog(
                    onDismissRequest = { discardTaskAlertDialogState = false },
                    onConfirmButtonClick = navigateBack,
                )
            }
        },
    )
    if (datePickerDialogState) {
        DatePickerDialog(
            onDismissRequest = { datePickerDialogState = false },
            onConfirm = {
                datePickerDialogState = false
                taskDueDate =
                    datePickerState.selectedDateMillis?.plus(timePickerState.selectedTimeMillis)
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
    if (timePickerDialogState) {
        TimePickerDialog(
            onDismissRequest = { timePickerDialogState = false },
            onConfirm = {
                timePickerDialogState = false
                taskDueDate =
                    datePickerState.selectedDateMillis?.plus(timePickerState.selectedTimeMillis)
            },
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

private fun initialValuesUpdated(
    taskTitle: String,
    taskDueDate: Long?,
    taskDescription: String,
    taskChecklistItems: List<String>,
) = taskTitle.isNotBlank() ||
    taskDueDate != null ||
    taskDescription.isNotBlank() ||
    taskChecklistItems.isNotEmpty()

@Composable
private fun FieldTitle(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(horizontal = SectionPadding),
    )
}

@Composable
private fun FieldTitle(
    text: AnnotatedString,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(horizontal = SectionPadding),
    )
}

@Composable
private fun TaskChecklistItem(
    taskChecklistItem: String,
    onDeleteTaskCheckListItem: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = SectionPadding, end = 8.dp),
    ) {
        Text(
            text = taskChecklistItem,
            modifier = Modifier.weight(1f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        IconButton(onClick = onDeleteTaskCheckListItem) {
            Icon(
                Images.Icons.Close,
                contentDescription = TodometerResources.strings.clear,
                tint = MaterialTheme.colorScheme.onSurface.applyMediumEmphasisAlpha(),
            )
        }
    }
}
