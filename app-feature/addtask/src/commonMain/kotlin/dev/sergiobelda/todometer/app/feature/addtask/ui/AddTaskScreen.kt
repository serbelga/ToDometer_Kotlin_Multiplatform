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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.sergiobelda.navigation.compose.extended.annotation.NavDestination
import dev.sergiobelda.todometer.app.common.designsystem.components.TodometerTitledTextField
import dev.sergiobelda.todometer.app.common.designsystem.theme.Alpha.applyMediumEmphasisAlpha
import dev.sergiobelda.todometer.app.common.ui.actions.SystemBackHandler
import dev.sergiobelda.todometer.app.common.ui.components.DatePickerDialog
import dev.sergiobelda.todometer.app.common.ui.components.SaveActionTopAppBar
import dev.sergiobelda.todometer.app.common.ui.components.TimePickerDialog
import dev.sergiobelda.todometer.app.common.ui.values.SectionPadding
import dev.sergiobelda.todometer.app.common.ui.values.TextFieldPadding
import dev.sergiobelda.todometer.app.feature.addtask.navigation.AddTaskNavigationEvent
import dev.sergiobelda.todometer.common.designsystem.resources.images.Images
import dev.sergiobelda.todometer.common.designsystem.resources.images.icons.Close
import dev.sergiobelda.todometer.common.resources.TodometerResources
import dev.sergiobelda.todometer.common.ui.base.BaseUI

@OptIn(ExperimentalMaterial3Api::class)
data object AddTaskScreen : BaseUI<AddTaskUIState, AddTaskContentState>() {

    @Composable
    override fun rememberContentState(
        uiState: AddTaskUIState,
    ): AddTaskContentState = rememberAddTaskContentState()

    @NavDestination(
        name = "AddTask",
        destinationId = "addtask",
        deepLinkUris = ["app://open.add.task"],
    )
    @Composable
    override fun Content(
        uiState: AddTaskUIState,
        contentState: AddTaskContentState,
    ) {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(contentState.topAppBarState)

        val onBack: () -> Unit = {
            onEvent(
                AddTaskEvent.OnBack(
                    navigateBack = {
                        onEvent(AddTaskNavigationEvent.NavigateBack)
                    },
                ),
            )
        }
        SystemBackHandler(onBack = onBack)

        if (uiState.errorUi != null) {
            LaunchedEffect(contentState.snackbarHostState) {
                contentState.showSnackbar(
                    message = uiState.errorUi.message ?: "",
                )
            }
        }

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(contentState.snackbarHostState) },
            topBar = {
                AddTaskTopBar(
                    navigateBack = onBack,
                    isSaveButtonEnabled = !uiState.isAddingTask,
                    onSaveButtonClick = {
                        /*if (taskTitle.isBlank()) {
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
                        }*/
                    },
                )
            },
            content = { paddingValues ->
                AddTaskContent(
                    showProgress = uiState.isAddingTask,
                    taskTitle = contentState.taskTitle,
                    modifier = Modifier
                        .padding(paddingValues),
                )
            },
        )
        if (contentState.discardTaskAlertDialogVisible) {
            DiscardTaskAlertDialog(
                onDismissRequest = { onEvent(AddTaskEvent.OnDismissDiscardTaskDialog) },
                onConfirmButtonClick = { onEvent(AddTaskNavigationEvent.NavigateBack) },
            )
        }
        if (contentState.datePickerDialogVisible) {
            DatePickerDialog(
                onDismissRequest = { onEvent(AddTaskEvent.OnDismissDatePickerDialog) },
                onConfirm = { onEvent(AddTaskEvent.OnConfirmDatePickerDialog) },
            ) {
                DatePicker(state = contentState.datePickerState)
            }
        }
        if (contentState.timePickerDialogVisible) {
            TimePickerDialog(
                onDismissRequest = { onEvent(AddTaskEvent.OnDismissTimePickerDialog) },
                onConfirm = { onEvent(AddTaskEvent.OnConfirmTimePickerDialog) },
            ) {
                TimePicker(state = contentState.timePickerState)
            }
        }
    }

    @Composable
    private fun AddTaskTopBar(
        navigateBack: () -> Unit,
        isSaveButtonEnabled: Boolean,
        onSaveButtonClick: () -> Unit,
    ) {
        SaveActionTopAppBar(
            navigateBack = navigateBack,
            title = TodometerResources.strings.addTask,
            isSaveButtonEnabled = isSaveButtonEnabled,
            onSaveButtonClick = onSaveButtonClick,
            saveButtonTintColor = if (isSaveButtonEnabled) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.applyMediumEmphasisAlpha()
            },
        )
    }

    @Composable
    private fun AddTaskContent(
        showProgress: Boolean,
        taskTitle: String,
        modifier: Modifier,
    ) {
        Column(
            modifier = modifier,
        ) {
            if (showProgress) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            LazyColumn {
                item {
                    TodometerTitledTextField(
                        title = TodometerResources.strings.name,
                        value = taskTitle,
                        onValueChange = {
                            onEvent(AddTaskEvent.TaskTitleValueChange(it))
                        },
                        placeholder = { Text(TodometerResources.strings.enterTaskName) },
                        // isError = taskTitleInputError,
                        errorMessage = TodometerResources.strings.fieldNotEmpty,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next,
                        ),
                        modifier = Modifier.padding(TextFieldPadding),
                    )
                }
                /*item {
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
                }*/
            }
        }
    }
}

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
