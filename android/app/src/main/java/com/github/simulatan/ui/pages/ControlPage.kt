package com.github.simulatan.ui.pages

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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import com.github.simulatan.matrixcontrol.protocol.message.parts.TransitionMessagePart
import com.github.simulatan.ui.components.MessageRowEditComponent
import com.github.simulatan.utils.AppPreferences
import com.github.simulatan.utils.MockSettings
import com.github.simulatan.utils.send
import kotlinx.coroutines.launch

@Composable
fun ControlPage(settings: AppPreferences, navController: NavController, messages: Messages) =
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
		}
	}


@Preview
@Composable
fun ControlPagePreview() = ControlPage(
	settings = MockSettings,
	rememberNavController(),
	mutableListOf(MessageRow.SAMPLE, MessageRow.SAMPLE)
)
