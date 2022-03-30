/*
 * Copyright 2021 Sergio Belda
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

package dev.sergiobelda.todometer.ui.edittasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.sergiobelda.todometer.common.domain.Result
import dev.sergiobelda.todometer.common.domain.model.Task
import dev.sergiobelda.todometer.common.domain.model.TaskList
import dev.sergiobelda.todometer.common.domain.usecase.GetTaskListSelectedUseCase
import dev.sergiobelda.todometer.common.domain.usecase.UpdateTaskListUseCase
import dev.sergiobelda.todometer.ui.error.ErrorUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditTaskListViewModel(
    getTaskListSelectedUseCase: GetTaskListSelectedUseCase,
    private val updateTaskListUseCase: UpdateTaskListUseCase
) : ViewModel() {

    val taskListSelected: StateFlow<Result<TaskList>> =
        getTaskListSelectedUseCase().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            Result.Loading
        )

    fun updateTaskList(taskList: TaskList) = viewModelScope.launch {
        updateTaskListUseCase(taskList)
    }
}

data class EditTaskUiState(
    val isLoading: Boolean = false,
    val task: Task? = null,
    val error: ErrorUi? = null
)
