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

package dev.sergiobelda.todometer.app.feature.addtasklist.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import dev.sergiobelda.todometer.common.ui.base.BaseContentState
import dev.sergiobelda.todometer.common.ui.base.BaseEvent

data class AddTaskListContentState internal constructor(
    val snackbarHostState: SnackbarHostState,
) : BaseContentState {
    var taskListName: String by mutableStateOf("")
        private set

    val isSaveButtonEnabled: Boolean by derivedStateOf { taskListName.isNotBlank() }

    override fun handleEvent(event: BaseEvent) {
        when (event) {
            is AddTaskListEvent.TaskListNameValueChange -> {
                taskListName = event.value
            }
        }
    }

    suspend fun showSnackbar(message: String) =
        snackbarHostState.showSnackbar(message = message)

    companion object {
        fun saver(
            snackbarHostState: SnackbarHostState,
        ): Saver<AddTaskListContentState, String> = Saver(
            save = {
                it.taskListName
            },
            restore = {
                AddTaskListContentState(
                    snackbarHostState = snackbarHostState,
                ).apply {
                    taskListName = it
                }
            },
        )
    }
}

@Composable
fun rememberAddTaskListContentState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
): AddTaskListContentState = rememberSaveable(
    saver = AddTaskListContentState.saver(
        snackbarHostState = snackbarHostState,
    ),
) {
    AddTaskListContentState(
        snackbarHostState = snackbarHostState,
    )
}
