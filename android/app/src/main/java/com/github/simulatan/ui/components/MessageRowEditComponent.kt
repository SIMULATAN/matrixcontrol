package com.github.simulatan.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import com.github.simulatan.utils.MockSettings
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
			rowEditCallback,
			baseModifier
		)
	}

	if (settings.tabletMode) {
		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			content(Modifier)
			DeleteComponent(index, rowEditCallback)
		}
	} else {
		// IntrinsicSize.Max makes the box as wide as it COULD be
		// this is WITH the delete icon
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.requiredWidth(IntrinsicSize.Max)
		) {
			Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
				content(
					if (index > 0) {
						Modifier.requiredWidth(IntrinsicSize.Min)
					} else {
						// there's no delete button => we fill the space of the spacer too
						Modifier.fillMaxWidth()
					}
				)
			}
			// the spacer emulates the icon
			if (index == 0) {
				Spacer(modifier = Modifier.width(48.dp))
			}
			DeleteComponent(index, rowEditCallback)
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
	rowEditCallback: (MessageRow?) -> Unit,
	@SuppressLint("ModifierParameter") baseModifier: Modifier
) {
	var text by remember {
		mutableStateOf(row.text)
	}

	if (settings.tabletMode) {
		Button(
			onClick = { navController.navigate(Page.TRANSITION_SELECT, index) },
			modifier = baseModifier
		) {
			Text(text = row.transition.fancyName)
			Icon(
				imageVector = Icons.Filled.ArrowDropDown,
				contentDescription = "change transition",
			)
		}
		Spacer(Modifier.width(5.dp))
	} else {
		var expanded by remember { mutableStateOf(false) }

		ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
			TextField(
				value = row.transition.fancyName,
				onValueChange = {},
				readOnly = true,
				trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) },
				// makes the field use as little space as possible
				modifier = baseModifier.menuAnchor().widthIn(min = 1.dp),
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

	val fieldModifier = if (index > 0) {
		baseModifier.widthIn(min = 200.dp, max = 400.dp)
	} else {
		baseModifier.width(IntrinsicSize.Min)
	}
	TextField(
		value = text,
		onValueChange = { text = it; rowEditCallback(row.copy(text = it)) },
		label = { Text("Text") },
		modifier = fieldModifier,
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
				settings = MockSettings.copy(tabletMode = parameters.tabletMode),
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
