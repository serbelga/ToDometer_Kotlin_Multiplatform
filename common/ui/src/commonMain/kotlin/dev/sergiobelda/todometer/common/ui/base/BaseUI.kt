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

package dev.sergiobelda.todometer.common.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

abstract class BaseUI<U : BaseUIState, C : BaseContentState> {

    private lateinit var eventHandler: BaseEventHandler

    @Composable
    abstract fun rememberContentState(
        uiState: U,
    ): C

    @Composable
    fun Content(
        viewModel: BaseViewModel<U>,
        onEvent: (BaseEvent) -> Unit = {},
    ) {
        val contentState = rememberContentState(
            uiState = viewModel.uiState,
        )
        eventHandler = remember {
            BaseEventHandler { event ->
                viewModel.handleEvent(event)
                contentState.handleEvent(event)
                onEvent.invoke(event)
            }
        }
        Content(
            uiState = viewModel.uiState,
            contentState = contentState,
        )
    }

    @Composable
    protected abstract fun Content(
        uiState: U,
        contentState: C,
    )

    protected fun onEvent(
        event: BaseEvent,
    ) {
        eventHandler.handleEvent(event)
    }
}
