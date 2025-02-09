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

package dev.sergiobelda.todometer.app.feature.edittasklist.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import dev.sergiobelda.navigation.compose.extended.annotation.NavDestination
import dev.sergiobelda.todometer.app.common.designsystem.components.TodometerTitledTextField
import dev.sergiobelda.todometer.app.common.designsystem.theme.Alpha.applyMediumEmphasisAlpha
import dev.sergiobelda.todometer.app.common.ui.components.SaveActionTopAppBar
import dev.sergiobelda.todometer.app.common.ui.loading.LoadingScreenDialog
import dev.sergiobelda.todometer.app.common.ui.values.TextFieldPadding
import dev.sergiobelda.todometer.app.feature.edittasklist.navigation.EditTaskListNavigationEvent
import dev.sergiobelda.todometer.common.resources.TodometerResources
import dev.sergiobelda.todometer.common.ui.base.BaseUI

data object EditTaskListScreen : BaseUI<EditTaskListUIState, EditTaskListContentState>() {

    @Composable
    override fun rememberContentState(
        uiState: EditTaskListUIState,
    ): EditTaskListContentState = rememberEditTaskListContentState(
        taskListNameInputValue = uiState.taskList?.name ?: "Default",
    )

    @NavDestination(
        destinationId = "edittasklist",
        name = "EditTaskList",
    )
    @Composable
    override fun Content(
        uiState: EditTaskListUIState,
        contentState: EditTaskListContentState,
    ) {
        when {
            uiState.isLoading -> {
                LoadingScreenDialog(
                    navigateBack = {
                        onEvent(
                            EditTaskListNavigationEvent.NavigateBack,
                        )
                    },
                )
            }

            !uiState.isLoading -> {
                Scaffold(
                    topBar = {
                        EditTaskListTopBar(
                            isSaveButtonEnabled = !uiState.isLoading,
                            onSaveButtonClick = {
                                // TODO
                                onEvent(
                                    EditTaskListEvent.UpdateTaskList(
                                        contentState.nameTextFieldValue,
                                    ),
                                )
                            },
                        )
                    },
                    content = { paddingValues ->
                        EditTaskListContent(
                            taskListNameValue = contentState.nameTextFieldValue,
                            modifier = Modifier
                                .padding(paddingValues),
                        )
                    },
                )
            }
        }
    }

    @Composable
    private fun EditTaskListTopBar(
        isSaveButtonEnabled: Boolean,
        onSaveButtonClick: () -> Unit,
    ) {
        SaveActionTopAppBar(
            navigateBack = {
                onEvent(
                    EditTaskListNavigationEvent.NavigateBack,
                )
            },
            title = TodometerResources.strings.editTaskList,
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
    private fun EditTaskListContent(
        taskListNameValue: String,
        modifier: Modifier,
    ) {
        Column(
            modifier = modifier,
        ) {
            TodometerTitledTextField(
                title = TodometerResources.strings.name,
                value = taskListNameValue,
                onValueChange = {
                    onEvent(EditTaskListEvent.TaskListNameValueChange(it))
                },
                placeholder = { Text(TodometerResources.strings.enterTaskListName) },
                singleLine = true,
                errorMessage = TodometerResources.strings.fieldNotEmpty,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done,
                ),
                modifier = Modifier.padding(TextFieldPadding),
            )
        }
    }
}
