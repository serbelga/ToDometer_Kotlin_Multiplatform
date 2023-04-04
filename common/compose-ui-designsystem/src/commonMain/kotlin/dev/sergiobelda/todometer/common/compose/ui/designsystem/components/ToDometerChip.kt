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

package dev.sergiobelda.todometer.common.compose.ui.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ToDometerChip(
    modifier: Modifier = Modifier,
    border: BorderStroke = ToDometerChipDefaults.borderStroke,
    color: Color = ToDometerChipDefaults.color,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        border = border,
        shape = MaterialTheme.shapes.small,
        color = color
    ) {
        Row(
            modifier = Modifier.padding(ToDometerChipContentPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }
    }
}

private object ToDometerChipDefaults {
    val borderStroke: BorderStroke
        @Composable get() = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline
        )

    val color: Color @Composable get() = MaterialTheme.colorScheme.surface
}

private val ToDometerChipContentPadding: PaddingValues = PaddingValues(6.dp)
