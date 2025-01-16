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

package dev.sergiobelda.todometer.common.domain.usecase.task

import dev.sergiobelda.todometer.common.domain.Result
import dev.sergiobelda.todometer.common.domain.model.Tag
import dev.sergiobelda.todometer.common.domain.model.Task
import dev.sergiobelda.todometer.common.domain.repository.ITaskRepository

class InsertTaskUseCase(private val taskRepository: ITaskRepository) {

    /**
     * Creates a new [Task] given a [title], [description] and [tag], in the task list [taskListId].
     */
    suspend operator fun invoke(
        taskListId: String,
        title: String,
        tag: Tag = Tag.GRAY,
        description: String? = null,
        dueDate: Long? = null,
    ): Result<String> = taskRepository.insertTask(
        title,
        tag,
        description,
        dueDate,
        taskListId,
    )
}
