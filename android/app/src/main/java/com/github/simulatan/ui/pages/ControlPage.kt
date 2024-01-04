package com.github.simulatan.ui.pages

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.simulatan.MessageRow
import com.github.simulatan.Messages
import com.github.simulatan.Preset
import com.github.simulatan.Presets
import com.github.simulatan.R
import com.github.simulatan.matrixcontrol.protocol.message.parts.TransitionMessagePart
import com.github.simulatan.ui.components.MessageRowEditComponent
import com.github.simulatan.utils.AppPreferences
import com.github.simulatan.utils.DefaultSettings
import com.github.simulatan.utils.send
import kotlinx.coroutines.launch

@Composable
fun ControlPage(
	settings: AppPreferences,
	navController: NavController,
	messages: Messages,
	presets: Presets
) =
	Column(
		verticalArrangement = Arrangement.spacedBy(10.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.widthIn(min = 700.dp)
	) {
		LazyColumn(verticalArrangement = Arrangement.spacedBy(5.dp)) {
			itemsIndexed(messages) { index, item ->
				MessageRowEditComponent(settings, navController, item, index) { newItem ->
					if (newItem != null) {
						messages[index] = newItem
					} else {
						messages.removeAt(index)
					}
				}
			}
		}
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(10.dp)
		) {
			Button(onClick = {
				messages.add(
					MessageRow(
						transition = TransitionMessagePart.STILLSTEHEND,
						text = ""
					)
				)
			}, modifier = Modifier.size(40.dp), contentPadding = PaddingValues(1.dp)) {
				Icon(
					imageVector = Icons.Filled.Add,
					contentDescription = "add item"
				)
			}
			val composableScope = rememberCoroutineScope()
			Button(onClick = { composableScope.launch { send(messages, settings) } }) {
				Text("Send!")
			}

			val isBookmark = remember {
				mutableStateOf(presets.any { it.messageRows == messages })
			}
			Bookmark(isBookmark, presets, messages)
		}
	}

@Composable
private fun Bookmark(isBookmark: MutableState<Boolean>, presets: Presets, messages: Messages) {
	var bookmarkPopupShown by remember { mutableStateOf(false) }

	Button(
		onClick = { bookmarkPopupShown = true },
		modifier = Modifier.size(40.dp),
		contentPadding = PaddingValues(1.dp)
	) {
		Icon(
			painter = painterResource(
				id =
				if (isBookmark.value) R.drawable.baseline_bookmark_24
				else R.drawable.baseline_bookmark_border_24
			),
			contentDescription = "Bookmark as Preset"
		)
	}

	if (bookmarkPopupShown) {
		var presetName by remember { mutableStateOf("") }

		AlertDialog(
			onDismissRequest = { bookmarkPopupShown = false },
			confirmButton = {
				TextButton(onClick = {
					bookmarkPopupShown = false
					presets.add(Preset(presetName, messages.toList()))
					isBookmark.value = true
					Log.println(Log.INFO, null, "Presets: ${presets.toList()} messages: ${messages.toList()}")
				}) {
					Text("Save")
				}
			},
			dismissButton = {
				TextButton(onClick = { bookmarkPopupShown = false }) {
					Text("Cancel")
				}
			},
			title = { Text("Save Preset") },
			// actually the body
			text = {
				val focusRequester = remember { FocusRequester() }
				TextField(
					label = { Text("Preset Name") },
					modifier = Modifier.focusRequester(focusRequester),
					value = presetName,
					onValueChange = { presetName = it }
				)
				LaunchedEffect(Unit) {
					focusRequester.requestFocus()
				}
			}
		)
	}
}

@Preview
@Composable
fun ControlPagePreview() = ControlPage(
	settings = DefaultSettings,
	rememberNavController(),
	mutableListOf(MessageRow.SAMPLE, MessageRow.SAMPLE2),
	mutableListOf(Preset.SAMPLE2)
)
