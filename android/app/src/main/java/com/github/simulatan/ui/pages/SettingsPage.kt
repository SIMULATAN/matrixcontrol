package com.github.simulatan.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.github.simulatan.matrixcontrol.relay_provider.api.Relay
import com.github.simulatan.matrixcontrol.relay_provider.api.RelayProvider
import com.github.simulatan.matrixcontrol.relay_provider.api.RelaySettings
import com.github.simulatan.ui.components.SwitchWithLabel
import com.github.simulatan.utils.AppPreferences
import com.github.simulatan.utils.DefaultSettings
import com.github.simulatan.utils.PickedRelay
import com.github.simulatan.utils.RelayRegistry
import java.util.logging.Logger
import kotlin.reflect.safeCast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(settings: AppPreferences, navController: NavController) {
	Column(
		verticalArrangement = Arrangement.spacedBy(10.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.width(IntrinsicSize.Max)
	) {
		val availableRelays = RelayRegistry.getAvailableRelays()
		var expanded by remember { mutableStateOf(false) }
		var relay by remember { mutableStateOf(settings.relay) }
		ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
			TextField(
				value = relay.getProvider()?.name ?: "",
				onValueChange = {},
				readOnly = true,
				trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
				modifier = Modifier
					.menuAnchor()
					.fillMaxWidth()
			)

			ExposedDropdownMenu(
				expanded = expanded,
				onDismissRequest = { expanded = false }
			) {
				availableRelays.forEach { item ->
					DropdownMenuItem(
						text = { Text(text = item.name) },
						onClick = {
							relay = PickedRelay.fromProvider(item)
							expanded = false
						}
					)
				}
			}
		}

		relay.getProvider()?.apply {
			@Suppress("UNCHECKED_CAST")
			@Composable
			fun <S : RelaySettings, R : Relay<S>> makeWidget(provider: RelayProvider<S, R>) {
				val providerSettings = provider.settingsClass.safeCast(relay.settings) ?: let {
					Logger.getLogger(javaClass.simpleName).warning("Settings not of expected type ${provider.settingsClass}: ${relay.settings}")
					provider.defaultSettings
				}
				provider.Widget(providerSettings) {
					relay.settings = it
				}
			}
			makeWidget(this)
		}

		var tabletMode by remember { mutableStateOf(settings.tabletMode) }
		SwitchWithLabel(
			"Tablet Mode",
			checked = tabletMode,
			onCheckedChange = { tabletMode = it },
			modifier = Modifier.fillMaxWidth()
		)

		Button(modifier = Modifier.fillMaxWidth(), onClick = {
			settings.tabletMode = tabletMode
			settings.relay = relay
			navController.navigateUp()
		}) {
			Text("Save")
		}
	}
}

@Preview(showBackground = true)
@Composable
fun SettingsPagePreview() = SettingsPage(
	settings = DefaultSettings,
	navController = rememberNavController()
)
