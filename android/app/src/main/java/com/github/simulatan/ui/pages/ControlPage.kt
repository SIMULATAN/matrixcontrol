package com.github.simulatan.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.simulatan.matrixcontrol.protocol.ProtocolBuilder
import com.github.simulatan.utils.AppPreferences
import com.github.simulatan.utils.MockSettings
import com.github.simulatan.utils.navigate
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.launch

@OptIn(ExperimentalUnsignedTypes::class)
@Composable
fun ControlPage(settings: AppPreferences, navController: NavController) = Column {
	val client = HttpClient()
	var text by remember { mutableStateOf("My Text") }

	suspend fun sendText() {
		val response = client.post("${settings.server}/raw") {
			setBody(ProtocolBuilder.of(text).toByteArray())
			header("Serial-Port", settings.serialPort)
		}
		println(response)
	}

	Column(
		verticalArrangement = Arrangement.spacedBy(10.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = Modifier.widthIn(min = 700.dp)
	) {
		Row(verticalAlignment = Alignment.CenterVertically) {
			IconButton(onClick = { navController.navigate(Page.TRANSITION_SELECT) }) {
				Icon(
					imageVector = Icons.Filled.ArrowDropDown,
					contentDescription = "change transition"
				)
			}
			Text(text = "PAC_MAN", color = Color.Red)
			Spacer(modifier = Modifier.width(8.dp))
			TextField(value = text, onValueChange = { text = it }, label = { Text("Text") })
		}
		val composableScope = rememberCoroutineScope()
		Button(onClick = { composableScope.launch { sendText() } }) {
			Text("Send!")
		}
	}
}

@Preview
@Composable
fun ControlPagePreview() = ControlPage(settings = MockSettings, rememberNavController())
