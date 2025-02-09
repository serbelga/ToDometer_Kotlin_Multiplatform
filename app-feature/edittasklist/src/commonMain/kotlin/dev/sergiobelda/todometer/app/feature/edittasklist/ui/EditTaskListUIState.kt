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

package dev.sergiobelda.todometer.app.feature.edittasklist.ui

import androidx.compose.runtime.Immutable
import dev.sergiobelda.todometer.common.domain.model.TaskList
import dev.sergiobelda.todometer.common.ui.base.BaseUIState
import dev.sergiobelda.todometer.common.ui.error.ErrorUi

@Immutable
data class EditTaskListUIState(
    val isLoading: Boolean = false,
    val taskList: TaskList? = null,
    val errorUi: ErrorUi? = null,
) : BaseUIState
