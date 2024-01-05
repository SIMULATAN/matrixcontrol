package com.github.simulatan

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.simulatan.ui.components.CustomAppBar
import com.github.simulatan.ui.pages.ControlPage
import com.github.simulatan.ui.pages.Page
import com.github.simulatan.ui.pages.PresetsPage
import com.github.simulatan.ui.pages.SettingsPage
import com.github.simulatan.ui.pages.TransitionSelectPage
import com.github.simulatan.ui.theme.MatrixcontrolTheme
import com.github.simulatan.utils.AppPreferences
import com.github.simulatan.utils.AppPreferencesImpl
import com.github.simulatan.utils.MockSettings
import com.github.simulatan.utils.dataStore
import com.github.simulatan.utils.navigate
import com.github.simulatan.utils.plus
import kotlinx.coroutines.flow.collectLatest


class MainActivity : ComponentActivity() {
	companion object {
		init {
			// broken for now
			System.loadLibrary("jSerialComm")
		}
	}

	private var messages = mutableStateListOf(MessageRow.SAMPLE)
	private var presets = mutableStateListOf(Preset.SAMPLE)

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		// `toList` is required because `SnapshotStateList` doesn't have a serializer
		outState.putObject("currentMessages", messages.toList())

		val presets = presets.toList()
		outState.putObject("presets", presets)
		baseContext.dataStore.setObject(presetsKey, presets)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		messages = savedInstanceState.getObject<ImmutableMessages?>("messages", null)?.toMutableStateList()
				?: mutableStateListOf(MessageRow.SAMPLE)

		presets =
			savedInstanceState.getObject<ImmutablePresets?>("presets", null)?.toMutableStateList()
			?: baseContext.dataStore.getObject<ImmutablePresets?>(presetsKey)?.toMutableStateList()
			?: mutableStateListOf(Preset.SAMPLE)

		setContent {
			// observes `presets` to auto-save on change
			// this is required because an app crash will discard all changes!
			LaunchedEffect(Unit) {
				snapshotFlow { presets.toList() }.collectLatest {
					baseContext.dataStore.setObject(presetsKey, it)
				}
			}

			val messages: Messages = remember { messages }
			val presets: Presets = remember { presets }

			MatrixcontrolTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					val appPreferences = AppPreferencesImpl(baseContext)
					MainComponent(appPreferences, messages, presets)
				}
			}
		}
	}
}

@Composable
fun MainComponent(appPreferences: AppPreferences, messages: Messages, presets: Presets) {
	val navController = rememberNavController()
	val backStackEntry by navController.currentBackStackEntryAsState()

	val currentScreen = Page.parse(backStackEntry?.destination?.route ?: Page.CONTROL.name)

	Scaffold(
		topBar = {
			CustomAppBar(
				settings = appPreferences,
				currentScreen = currentScreen,
				navController = navController
			)
		},
		bottomBar = {
			BottomAppBar {
				listOf(Page.SETTINGS, Page.CONTROL, Page.PRESETS).forEach {
					NavigationBarItem(
						selected = currentScreen == it,
						onClick = { navController.navigate(it) },
						icon = it.icon(),
						label = { Text(it.title) }
					)
				}
			}
		}
	) { innerPadding ->
		Column(
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier
				.padding(innerPadding + 8.dp)
				.fillMaxSize()
		) {
			NavHost(
				navController = navController,
				startDestination = Page.CONTROL.name
			) {
				composable(Page.SETTINGS.route) {
					SettingsPage(appPreferences, navController)
				}
				composable(Page.CONTROL.route) {
					ControlPage(appPreferences, navController, messages, presets)
				}
				composable(
					Page.TRANSITION_SELECT.route,
					arguments = Page.TRANSITION_SELECT.arguments
				) {
					TransitionSelectPage(
						navController,
						messages,
						it.arguments?.getInt(Page.TRANSITION_SELECT.arguments.first().name)!!
					)
				}
				composable(Page.PRESETS.route) {
					PresetsPage(appPreferences, navController, messages, presets)
				}
			}
		}
	}
}

@Preview(
	showBackground = true,
	uiMode = Configuration.UI_MODE_NIGHT_YES,
	widthDp = 1920,
	heightDp = 1080
)
@Composable
fun MainPreview() {
	MatrixcontrolTheme {
		MainComponent(
			MockSettings.TabletMode,
			mutableListOf(MessageRow.SAMPLE),
			mutableListOf(Preset.SAMPLE)
		)
	}
}
