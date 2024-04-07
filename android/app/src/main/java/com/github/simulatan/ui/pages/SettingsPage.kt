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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.simulatan.matrixcontrol.relay_provider.api.PickedRelay
import com.github.simulatan.matrixcontrol.relay_provider.api.RelayViewModel
import com.github.simulatan.matrixcontrol.relay_provider.api.RelayViewModelFactory
import com.github.simulatan.ui.components.SwitchWithLabel
import com.github.simulatan.utils.AppPreferences
import com.github.simulatan.utils.DefaultSettings
import com.github.simulatan.utils.RelayRegistry
import com.github.simulatan.utils.getProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(settings: AppPreferences, navController: NavController) {
	Column(
		verticalArrangement = Arrangement.spacedBy(10.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.width(IntrinsicSize.Max)
	) {
		val relayViewModel: RelayViewModel =
			viewModel(factory = RelayViewModelFactory(settings.relay))

		val availableRelays = RelayRegistry.getAvailableRelays()
		var expanded by remember { mutableStateOf(false) }
		val relay by relayViewModel.pickedRelay
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
							relayViewModel.updateRelay(PickedRelay.fromProvider(item))
							expanded = false
						}
					)
				}
			}
		}

		relay.getProvider()?.apply {
			this.Widget(relayViewModel)
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
			settings.relay = relayViewModel.pickedRelay.value
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
