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

package dev.sergiobelda.todometer.app.feature.addtasklist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import dev.sergiobelda.navigation.compose.extended.annotation.NavDestination
import dev.sergiobelda.todometer.app.common.designsystem.components.TodometerTitledTextField
import dev.sergiobelda.todometer.app.common.designsystem.theme.Alpha.applyMediumEmphasisAlpha
import dev.sergiobelda.todometer.app.common.ui.components.SaveActionTopAppBar
import dev.sergiobelda.todometer.app.common.ui.values.TextFieldPadding
import dev.sergiobelda.todometer.app.feature.addtasklist.navigation.AddTaskListNavigationEvent
import dev.sergiobelda.todometer.common.resources.TodometerResources
import dev.sergiobelda.todometer.common.ui.base.BaseUI

data object AddTaskListScreen : BaseUI<AddTaskListUIState, AddTaskListContentState>() {

    @Composable
    override fun rememberContentState(
        uiState: AddTaskListUIState,
    ): AddTaskListContentState = rememberAddTaskListContentState()

    @NavDestination(
        name = "AddTaskList",
        destinationId = "addtasklist",
    )
    @Composable
    override fun Content(
        uiState: AddTaskListUIState,
        contentState: AddTaskListContentState,
    ) {
        if (uiState.errorUi != null) {
            LaunchedEffect(contentState.snackbarHostState) {
                contentState.showSnackbar(
                    message = uiState.errorUi.message ?: "",
                )
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(contentState.snackbarHostState) },
            topBar = {
                AddTaskListTopBar(
                    isSaveButtonEnabled = !uiState.isAddingTaskList && contentState.isSaveButtonEnabled,
                    onSaveButtonClick = {
                        onEvent(
                            AddTaskListEvent.InsertTaskList(contentState.taskListName),
                        )
                        onEvent(
                            AddTaskListNavigationEvent.NavigateBack,
                        )
                    },
                )
            },
            content = { paddingValues ->
                AddTaskListContent(
                    showProgress = uiState.isAddingTaskList,
                    taskListNameValue = contentState.taskListName,
                    modifier = Modifier.padding(paddingValues),
                )
            },
        )
    }

    @Composable
    private fun AddTaskListTopBar(
        isSaveButtonEnabled: Boolean,
        onSaveButtonClick: () -> Unit,
    ) {
        SaveActionTopAppBar(
            navigateBack = {
                onEvent(
                    AddTaskListNavigationEvent.NavigateBack,
                )
            },
            title = TodometerResources.strings.addTaskList,
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
    private fun AddTaskListContent(
        showProgress: Boolean,
        taskListNameValue: String,
        modifier: Modifier,
    ) {
        Column(
            modifier = modifier,
        ) {
            if (showProgress) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            TodometerTitledTextField(
                title = TodometerResources.strings.name,
                value = taskListNameValue,
                onValueChange = {
                    onEvent(AddTaskListEvent.TaskListNameValueChange(it))
                },
                placeholder = { Text(TodometerResources.strings.enterTaskListName) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done,
                ),
                modifier = Modifier.padding(TextFieldPadding),
            )
        }
    }
}
