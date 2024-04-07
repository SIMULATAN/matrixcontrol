package com.github.simulatan.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
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

fun NavController.navigate(page: Page, vararg parameters: Any) =
	navigate(page.routeParams(*parameters))

fun TextStyle.fixVerticalAlign() = copy(
	lineHeightStyle = LineHeightStyle(
		alignment = LineHeightStyle.Alignment.Proportional,
		trim = LineHeightStyle.Trim.None
	)
)

suspend fun tryOrShowError(snackbarHostState: SnackbarHostState, block: suspend () -> Unit) {
	try {
		block()
	} catch (exception: Exception) {
		snackbarHostState.showSnackbar(
			message = exception.message ?: "${exception.javaClass.simpleName}: Unknown error occurred.",
			withDismissAction = true
		)
	}
}
