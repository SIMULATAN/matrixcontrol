package com.github.simulatan

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.simulatan.ui.components.ControlComponent
import com.github.simulatan.ui.theme.MatrixcontrolTheme
import com.github.simulatan.utils.AppPreferences
import com.github.simulatan.utils.AppPreferencesImpl


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
					MainComponent(AppPreferencesImpl(baseContext))
				}
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainComponent(appPreferences: AppPreferences) = Scaffold(
	topBar = {
		TopAppBar(title = { Text("MatrixControl") })
	}
) { innerPadding ->
	Column(
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.padding(innerPadding).fillMaxSize()
	) {
		Column(modifier = Modifier.padding(8.dp)) {
			ControlComponent(appPreferences)
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