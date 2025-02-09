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

package dev.sergiobelda.todometer.app.feature.settings.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.sergiobelda.navigation.compose.extended.annotation.NavDestination
import dev.sergiobelda.todometer.app.common.designsystem.theme.Alpha
import dev.sergiobelda.todometer.app.common.designsystem.theme.withAlpha
import dev.sergiobelda.todometer.app.common.ui.preferences.themeIcon
import dev.sergiobelda.todometer.app.common.ui.preferences.themeName
import dev.sergiobelda.todometer.app.feature.settings.navigation.SettingsNavigationEvent
import dev.sergiobelda.todometer.common.designsystem.resources.images.Images
import dev.sergiobelda.todometer.common.designsystem.resources.images.icons.CheckCircle
import dev.sergiobelda.todometer.common.designsystem.resources.images.icons.NavigateBefore
import dev.sergiobelda.todometer.common.domain.preference.AppTheme
import dev.sergiobelda.todometer.common.resources.TodometerResources
import dev.sergiobelda.todometer.common.ui.base.BaseUI
import dev.sergiobelda.todometer.common.ui.base.DefaultContentState

data object SettingsScreen : BaseUI<SettingsUIState, DefaultContentState>() {

    @Composable
    override fun rememberContentState(
        uiState: SettingsUIState,
    ): DefaultContentState = DefaultContentState

    @NavDestination(
        destinationId = "settings",
        name = "Settings",
    )
    @Composable
    override fun Content(
        uiState: SettingsUIState,
        contentState: DefaultContentState,
    ) {
        Scaffold(
            topBar = {
                SettingsTopBar()
            },
            content = { paddingValues ->
                SettingsContent(
                    appTheme = uiState.appTheme,
                    modifier = Modifier.padding(paddingValues),
                )
            },
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SettingsTopBar() {
        TopAppBar(
            navigationIcon = {
                IconButton(
                    onClick = {
                        onEvent(SettingsNavigationEvent.NavigateBack)
                    },
                ) {
                    Icon(
                        Images.Icons.NavigateBefore,
                        contentDescription = TodometerResources.strings.back,
                    )
                }
            },
            title = {
                Text(TodometerResources.strings.settings)
            },
        )
    }

    @Composable
    private fun SettingsContent(
        appTheme: AppTheme,
        modifier: Modifier,
    ) {
        Column(
            modifier = modifier,
        ) {
            SettingsChooseAppTheme(
                appTheme = appTheme,
                onItemClick = {
                    onEvent(SettingsEvent.SetAppTheme(it))
                },
            )
        }
    }
}

@Composable
private fun SettingsChooseAppTheme(appTheme: AppTheme, onItemClick: (AppTheme) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(SettingsItemPaddingHorizontal)) {
        Text(text = TodometerResources.strings.theme)
        Spacer(modifier = Modifier.height(SettingsItemInternalSpacing))
        LazyVerticalGrid(
            horizontalArrangement = Arrangement.spacedBy(AppThemeItemsSpacing),
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Adaptive(minSize = AppThemeItemWidth),
        ) {
            items(enumValues<AppTheme>()) {
                AppThemeItem(
                    it,
                    onClick = { onItemClick(it) },
                    it == appTheme,
                )
            }
        }
    }
}

@Composable
private fun AppThemeItem(
    appTheme: AppTheme,
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    val selectedAlpha = if (selected) Alpha.High else Alpha.Medium
    Box {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(AppThemeItemCornerRadius),
            border = BorderStroke(
                width = AppThemeItemBorder,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
            ),
            modifier = modifier
                .height(AppThemeItemHeight)
                .fillMaxWidth()
                .padding(AppThemeItemPadding),
            tonalElevation = if (selected) AppThemeItemSelectedElevation else AppThemeItemUnselectedElevation,
            contentColor = MaterialTheme.colorScheme.onSurface.withAlpha(selectedAlpha),
        ) {
            Box {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    Icon(
                        appTheme.themeIcon(),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.height(AppThemeItemSelectedSpacing))
                    Text(
                        appTheme.themeName(),
                        textAlign = TextAlign.Center,
                        maxLines = AppThemeItemMaxLines,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
        AppThemeItemSelectorIndicator(
            selected = selected,
        )
    }
}

@Composable
private fun BoxScope.AppThemeItemSelectorIndicator(
    selected: Boolean,
) {
    AnimatedVisibility(
        visible = selected,
        enter = scaleIn(
            animationSpec = tween(
                durationMillis = AppThemeItemSelectedIconAnimationDuration,
                easing = FastOutSlowInEasing,
            ),
        ),
        exit = fadeOut(),
    ) {
        Icon(
            Images.Icons.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .size(AppThemeItemSelectedIconSize)
                .align(Alignment.TopStart),
        )
    }
}

private val SettingsItemPaddingHorizontal: Dp = 16.dp
private val SettingsItemInternalSpacing: Dp = 16.dp

private val AppThemeItemsSpacing: Dp = 12.dp

private const val AppThemeItemMaxLines: Int = 2
private val AppThemeItemBorder: Dp = 1.dp
private val AppThemeItemCornerRadius: Dp = 8.dp
private val AppThemeItemHeight: Dp = 96.dp
private val AppThemeItemSelectedElevation: Dp = 6.dp
private const val AppThemeItemSelectedIconAnimationDuration: Int = 150
private val AppThemeItemPadding: Dp = 2.dp
private val AppThemeItemSelectedIconSize: Dp = 20.dp
private val AppThemeItemSelectedSpacing: Dp = 6.dp
private val AppThemeItemUnselectedElevation: Dp = 0.dp
private val AppThemeItemWidth: Dp = 96.dp
