/*
 * Copyright 2022 Sergio Belda
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

package dev.sergiobelda.todometer.common.database.mapper

import dev.sergiobelda.todometer.common.database.TaskChecklistItemEntity
import dev.sergiobelda.todometer.common.domain.model.TaskChecklistItem

fun TaskChecklistItemEntity.asTaskChecklistItem(): TaskChecklistItem =
    TaskChecklistItem(
        id = id,
        text = text,
        taskId = task_id,
        state = state,
    )

fun Iterable<TaskChecklistItemEntity>.asTaskChecklist(): List<TaskChecklistItem> = this.map {
    it.asTaskChecklistItem()
}

fun TaskChecklistItem.asTaskChecklistItemEntity(): TaskChecklistItemEntity =
    TaskChecklistItemEntity(
        id = id,
        text = text,
        task_id = taskId,
        state = state,
    )
