package com.github.simulatan.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
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
import com.github.simulatan.utils.tryOrShowError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PresetsPage(
	settings: AppPreferences,
	navController: NavController,
	snackbarHostState: SnackbarHostState,
	messages: Messages,
	presets: Presets
) {
	val scope = rememberCoroutineScope { Dispatchers.IO }
	val presetSelected: (Preset) -> Unit = { preset ->
		messages.clear()
		messages.addAll(preset.messageRows)

		if (settings.presets_instantApply) {
			scope.launch {
				tryOrShowError(snackbarHostState) {
					send(preset.messageRows, settings)
				}
			}
		}
		if (!settings.presets_stayOnPage) {
			navController.navigate(Page.CONTROL)
		}
	}
	if (settings.tabletMode) TabletLayout(presets, presetSelected)
	else PhoneLayout(presets, presetSelected)
}

@Composable
private fun TabletLayout(presets: Presets, presetSelected: (Preset) -> Unit) {
	LazyHorizontalGrid(
		rows = GridCells.Adaptive(minSize = 140.dp),
		contentPadding = PaddingValues(20.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp),
		horizontalArrangement = Arrangement.spacedBy(16.dp)
	) {
		items(presets) {
			Card(
				modifier = Modifier
					.padding(4.dp),
				onClick = { presetSelected(it) }
			) {
				Column(
					modifier = Modifier.fillMaxHeight(),
					verticalArrangement = Arrangement.Center
				) {
					Text(
						text = it.name,
						modifier = Modifier
							.padding(16.dp)
							.width(320.dp)
							.wrapContentHeight(align = Alignment.CenterVertically),
						style = Typography.displayMedium.copy(
							lineHeightStyle = LineHeightStyle(
								alignment = LineHeightStyle.Alignment.Bottom,
								trim = LineHeightStyle.Trim.None
							)
						),
						textAlign = TextAlign.Center
					)
				}
			}
		}
	}
}

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
					modifier = Modifier
						.fillMaxWidth()
						.padding(5.dp),
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
				snackbarHostState = SnackbarHostState(),
				messages = mutableListOf(MessageRow.SAMPLE, MessageRow.SAMPLE2),
				presets = mutableListOf(Preset.SAMPLE, Preset.SAMPLE2)
			)
		}
}
