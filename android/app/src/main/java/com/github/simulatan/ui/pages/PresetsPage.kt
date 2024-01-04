package com.github.simulatan.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.simulatan.MessageRow
import com.github.simulatan.Messages
import com.github.simulatan.Preset
import com.github.simulatan.Presets
import com.github.simulatan.ui.theme.Typography
import com.github.simulatan.utils.AppPreferences
import com.github.simulatan.utils.MockSettings
import com.github.simulatan.utils.fixVerticalAlign
import com.github.simulatan.utils.navigate
import com.github.simulatan.utils.send
import kotlinx.coroutines.launch

@Composable
fun PresetsPage(
	settings: AppPreferences,
	navController: NavController,
	messages: Messages,
	presets: Presets
) {
	val composableScope = rememberCoroutineScope()
	val presetSelected: (Preset) -> Unit = { preset ->
		messages.clear()
		messages.addAll(preset.messageRows)

		if (settings.presets_instantApply) {
			composableScope.launch {
				send(preset.messageRows, settings)
			}
		}
		if (!settings.presets_stayOnPage) {
			navController.navigate(Page.CONTROL)
		}
	}
	// TODO: tablet mode
	if (false && settings.tabletMode) return
	else PhoneLayout(presets, presetSelected)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhoneLayout(presets: Presets, presetSelected: (Preset) -> Unit) {
	LazyColumn {
		items(presets) { preset ->
			Card(
				modifier = Modifier
					.fillMaxWidth()
					.padding(5.dp),
				shape = MaterialTheme.shapes.small,
				onClick = { presetSelected(preset) }
			) {
				Row(
					modifier = Modifier.fillMaxWidth().padding(5.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween
				) {
					Text(
						preset.name,
						style = Typography.headlineMedium
							.fixVerticalAlign()
							.copy(color = MaterialTheme.colorScheme.onSurface),
						modifier = Modifier.padding(15.dp)
					)
					IconButton(onClick = { presets.remove(preset) }) {
						Icon(
							imageVector = Icons.Filled.Delete,
							contentDescription = "Delete Entry",
							tint = MaterialTheme.colorScheme.onSurface
						)
					}
				}
			}
		}
	}
}

@Preview
@Composable
fun PresetsPagePreview() {
	listOf(MockSettings.TabletMode, MockSettings.NoTabletMode)
		.map { settings ->
			PresetsPage(
				settings = settings,
				navController = rememberNavController(),
				messages = mutableListOf(MessageRow.SAMPLE, MessageRow.SAMPLE2),
				presets = mutableListOf(Preset.SAMPLE, Preset.SAMPLE2)
			)
		}
}
