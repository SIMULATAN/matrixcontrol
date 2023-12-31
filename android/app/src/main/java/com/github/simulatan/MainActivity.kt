package com.github.simulatan

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.github.simulatan.ui.pages.SettingsPage
import com.github.simulatan.ui.pages.TransitionSelectPage
import com.github.simulatan.ui.theme.MatrixcontrolTheme
import com.github.simulatan.utils.AppPreferences
import com.github.simulatan.utils.AppPreferencesImpl
import com.github.simulatan.utils.plus


class MainActivity : ComponentActivity() {
	companion object {
		init {
			// broken for now
			System.loadLibrary("jSerialComm")
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContent {
			MatrixcontrolTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					val appPreferences = AppPreferencesImpl(baseContext)
					MainComponent(appPreferences = appPreferences)
				}
			}
		}
	}
}

@Composable
fun MainComponent(appPreferences: AppPreferences) {
	val navController = rememberNavController()
	val backStackEntry by navController.currentBackStackEntryAsState()

	val currentScreen = Page.parse(backStackEntry?.destination?.route ?: Page.CONTROL.name)

	val messages: Messages = remember { mutableStateListOf(MessageRow.SAMPLE) }

	Scaffold(
		topBar = {
			CustomAppBar(
				currentScreen = currentScreen,
				navController = navController
			)
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
				composable(Page.CONTROL.name) {
					ControlPage(appPreferences, navController, messages)
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
				composable(Page.SETTINGS.name) {
					SettingsPage(appPreferences, navController)
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
		MainComponent(object : AppPreferences {
			override var server = "http://localhost:7070"
			override var serialPort = "/dev/ttyUSB0"
		})
	}
}
