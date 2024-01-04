package com.github.simulatan.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.simulatan.MessageRow
import com.github.simulatan.matrixcontrol.protocol.message.parts.TransitionMessagePart
import com.github.simulatan.ui.pages.Page
import com.github.simulatan.utils.AppPreferences
import com.github.simulatan.utils.DefaultSettings
import com.github.simulatan.utils.navigate

@Composable
fun MessageRowEditComponent(
	settings: AppPreferences,
	navController: NavController,
	row: MessageRow,
	index: Int,
	rowEditCallback: (MessageRow?) -> Unit
) {
	val content: @Composable (Modifier) -> Unit = @Composable { baseModifier ->
		ChildComponent(
			row,
			settings,
			navController,
			index,
			rowEditCallback
		)
	}

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(10.dp),
		modifier = Modifier.fillMaxWidth()
	) {
		if (settings.tabletMode) {
			DeleteComponent(index, rowEditCallback)
			content(Modifier)
		} else {
			DeleteComponent(index, rowEditCallback)
			Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
				content(Modifier)
			}
		}
	}
}

@Composable
private fun DeleteComponent(index: Int, rowEditCallback: (MessageRow?) -> Unit) {
	if (index > 0) {
		IconButton(onClick = { rowEditCallback(null) }, modifier = Modifier.size(48.dp)) {
			Icon(
				imageVector = Icons.Filled.Delete,
				contentDescription = "remove row"
			)
		}
	}
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ChildComponent(
	row: MessageRow,
	settings: AppPreferences,
	navController: NavController,
	index: Int,
	rowEditCallback: (MessageRow?) -> Unit
) {
	var text by remember {
		mutableStateOf(row.text)
	}

	if (settings.tabletMode) {
		Button(
			onClick = { navController.navigate(Page.TRANSITION_SELECT, index) },
			// make the button the same height as the parent
			modifier = Modifier.height(IntrinsicSize.Min)
		) {
			Text(text = row.transition.fancyName)
			Icon(
				imageVector = Icons.Filled.ArrowDropDown,
				contentDescription = "change transition",
			)
		}
	} else {
		var expanded by remember { mutableStateOf(false) }

		ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
			TextField(
				value = row.transition.fancyName,
				onValueChange = {},
				readOnly = true,
				trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
				modifier = Modifier.menuAnchor().fillMaxWidth()
			)

			ExposedDropdownMenu(
				expanded = expanded,
				onDismissRequest = { expanded = false }
			) {
				TransitionMessagePart.entries.forEach { item ->
					DropdownMenuItem(
						text = { Text(text = item.fancyName) },
						onClick = {
							rowEditCallback(row.copy(transition = item))
							expanded = false
						}
					)
				}
			}
		}
	}

	TextField(
		value = text,
		onValueChange = { text = it; rowEditCallback(row.copy(text = it)) },
		label = { Text("Text") },
		modifier = Modifier.fillMaxWidth(),
		singleLine = true
	)
}


data class PreviewParams(val rows: Int, val tabletMode: Boolean)

@Preview
@Composable
fun MessageRowEditComponentPreview(
	@PreviewParameter(PreviewParameters::class) parameters: PreviewParams,
) {
	Column {
		(0..<parameters.rows).map {
			MessageRowEditComponent(
				settings = DefaultSettings.copy(tabletMode = parameters.tabletMode),
				navController = rememberNavController(),
				row = MessageRow.SAMPLE.copy(
					transition = TransitionMessagePart.entries[it % TransitionMessagePart.entries.size],
					text = "Row #$it"
				),
				index = it,
				rowEditCallback = {}
			)
		}
	}
}

private class PreviewParameters : PreviewParameterProvider<PreviewParams> {
	override val values = sequenceOf(
		PreviewParams(1, false),
		PreviewParams(1, true),
		PreviewParams(2, false),
		PreviewParams(2, true)
	)
}
