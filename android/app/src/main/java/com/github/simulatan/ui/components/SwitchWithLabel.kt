package com.github.simulatan.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

// original version: https://stackoverflow.com/a/73076422/21038281

@Composable
fun SwitchWithLabel(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
	val interactionSource = remember { MutableInteractionSource() }
	Row(
		modifier = modifier
			.clickable(
				interactionSource = interactionSource,
				// This is for removing ripple when Row is clicked
				indication = null,
				role = Role.Switch,
				onClick = {
					onCheckedChange(!checked)
				}
			)
			.padding(8.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {

		Text(text = label)
		Spacer(modifier = Modifier.padding(start = 8.dp))
		Switch(
			checked = checked,
			onCheckedChange = {
				onCheckedChange(it)
			}
		)
	}
}
