package com.github.simulatan.ui.pages

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument

enum class Page(val title: String, val navigationIcon: @Composable (NavController) -> Unit = {}) {
	CONTROL("Control", navigationIcon = {
		IconButton(onClick = { it.navigate(SETTINGS.name) }) {
			Icon(
				imageVector = Icons.Filled.Settings, contentDescription = "app settings"
			)
		}
	}),
	TRANSITION_SELECT("Select a Transition") {
		override val route = "$name/{rowIndex}"
		override val arguments = listOf(navArgument("rowIndex") { type = NavType.IntType })

		/**
		 * @param params [0]: Int = rowIndex
		 */
		override fun routeParams(vararg params: Any) = "$name/${params[0]}"
	},
	SETTINGS("Settings");

	open val route = name
	open fun routeParams(vararg params: Any) = route
	open val arguments: List<NamedNavArgument> = listOf()

	companion object {
		fun parse(name: String): Page = entries.first { it.name == name || it.route == name }
	}
}
