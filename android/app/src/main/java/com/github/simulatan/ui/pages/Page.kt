package com.github.simulatan.ui.pages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

enum class Page(val title: String, val navigationIcon: @Composable (NavController) -> Unit = {}) {
	CONTROL("Control", navigationIcon = {
		IconButton(onClick = { it.navigate(SETTINGS.name) }) {
			Icon(
				imageVector = Icons.Filled.Settings,
				contentDescription = "app settings"
			)
		}
	}),
	TRANSITION_SELECT("Select a Transition"),
	SETTINGS("Settings");
}
