package com.github.simulatan.ui.pages

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.github.simulatan.R
import com.github.simulatan.ui.components.SwitchWithLabel
import com.github.simulatan.utils.AppPreferences

enum class Page(
	val title: String,
	val iconVector: ImageVector? = null,
	val navigationIcon: @Composable (NavController) -> Unit = {},
	val actions: @Composable RowScope.(NavController, AppPreferences) -> Unit
		= {_, _ ->}
) {
	SETTINGS("Settings", iconVector = Icons.Filled.Settings),
	CONTROL("Control", iconVector = Icons.Filled.Edit),
	TRANSITION_SELECT("Select a Transition", navigationIcon = {
		IconButton(onClick = { it.navigateUp() }) {
			Icon(
				imageVector = Icons.Filled.ArrowBack,
				contentDescription = "back"
			)
		}
	}) {
		override val route = "$name/{rowIndex}"
		override val arguments =
			listOf(navArgument("rowIndex") { type = NavType.IntType })

		/**
		 * @param params [0]: Int = rowIndex
		 */
		override fun routeParams(vararg params: Any) = "$name/${params[0]}"
	},
	PRESETS("Presets", actions = { _, settings ->
		var instantApply by remember { mutableStateOf(settings.presets_instantApply) }
		SwitchWithLabel(
			label = "Instant Apply",
			checked = instantApply,
			onCheckedChange = {
				instantApply = !instantApply
				settings.presets_instantApply = instantApply
			}
		)

		var stayOnPage by remember { mutableStateOf(settings.presets_stayOnPage) }
		SwitchWithLabel(
			label = "Stay on page",
			checked = stayOnPage,
			onCheckedChange = {
				stayOnPage = !stayOnPage
				settings.presets_stayOnPage = stayOnPage
			}
		)
	}) {
		override fun icon() = @Composable {
			Icon(
				painter = painterResource(id = R.drawable.baseline_grid_view_24),
				contentDescription = title
			)
		}
	};

	open val route = name
	open fun routeParams(vararg params: Any) = route
	open val arguments: List<NamedNavArgument> = listOf()

	open fun icon() = @Composable {
		Icon(contentDescription = title, imageVector = iconVector!!)
	}

	companion object {
		fun parse(name: String): Page = entries.first { it.name == name || it.route == name }
	}
}
