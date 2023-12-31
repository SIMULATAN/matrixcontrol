package com.github.simulatan.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
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
import com.github.simulatan.ui.components.MessageRowEditComponent
import com.github.simulatan.utils.AppPreferences
import com.github.simulatan.utils.MockSettings
import com.github.simulatan.utils.send
import kotlinx.coroutines.launch

@Composable
fun ControlPage(settings: AppPreferences, navController: NavController, messages: Messages) = Column {
	Column(
		verticalArrangement = Arrangement.spacedBy(10.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.widthIn(min = 700.dp)
	) {
		LazyColumn {
			itemsIndexed(messages) { index, item ->
				MessageRowEditComponent(navController, item, index) { newItem ->
					messages[index] = newItem
				}
			}
		}
		val composableScope = rememberCoroutineScope()
		Button(onClick = { composableScope.launch { send(messages, settings) } }) {
			Text("Send!")
		}
	}
}


@Preview
@Composable
fun ControlPagePreview()
	= ControlPage(settings = MockSettings, rememberNavController(), mutableListOf(MessageRow.SAMPLE))
