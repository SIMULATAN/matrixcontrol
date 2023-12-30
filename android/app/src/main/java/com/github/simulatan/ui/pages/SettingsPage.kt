package com.github.simulatan.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.simulatan.utils.AppPreferences
import com.github.simulatan.utils.MockSettings

@Composable
fun SettingsPage(settings: AppPreferences, navController: NavController) {
	Column(
		verticalArrangement = Arrangement.spacedBy(10.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.width(IntrinsicSize.Max)
	) {
		var server by remember { mutableStateOf(settings.server) }
		TextField(
			value = server,
			onValueChange = { server = it },
			label = { Text("Server") })

		var serialPort by remember { mutableStateOf(settings.serialPort) }
		TextField(
			value = serialPort,
			onValueChange = { serialPort = it },
			label = { Text("Serial Port") }
		)

		Button(modifier = Modifier.fillMaxWidth(), onClick = {
			settings.server = server
			settings.serialPort = serialPort
			navController.navigateUp()
		}) {
			Text("Save")
		}
	}
}

@Preview
@Composable
fun SettingsPagePreview() = SettingsPage(settings = MockSettings, navController = rememberNavController())
