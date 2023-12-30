package com.github.simulatan.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import com.github.simulatan.ui.pages.Page

operator fun PaddingValues.plus(other: Dp): PaddingValues = plus(PaddingValues(other))

operator fun PaddingValues.plus(other: PaddingValues): PaddingValues = PaddingValues(
	start = this.calculateStartPadding(LayoutDirection.Ltr) +
		other.calculateStartPadding(LayoutDirection.Ltr),
	top = this.calculateTopPadding() + other.calculateTopPadding(),
	end = this.calculateEndPadding(LayoutDirection.Ltr) +
		other.calculateEndPadding(LayoutDirection.Ltr),
	bottom = this.calculateBottomPadding() + other.calculateBottomPadding(),
)

fun NavController.navigate(page: Page) = navigate(page.name)
