package com.github.simulatan.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.github.simulatan.matrixcontrol.protocol.ProtocolBuilder
import com.github.simulatan.utils.AppPreferences
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.launch

@OptIn(ExperimentalUnsignedTypes::class)
@Composable
fun ControlComponent(settings: AppPreferences) = Column {
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
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		// extra field required because android sucks ass and can't even do basic string bindings
		var server by remember { mutableStateOf(settings.server) }
		TextField(
			value = server,
			onValueChange = { server = it; settings.server = it },
			label = { Text("Server") })

		var serialPort by remember { mutableStateOf(settings.serialPort) }
		TextField(
			value = serialPort,
			onValueChange = { serialPort = it; settings.serialPort = it },
			label = { Text("Serial Port") }
		)

		TextField(value = text, onValueChange = { text = it }, label = { Text("Text") })
		val composableScope = rememberCoroutineScope()
		Button(onClick = { composableScope.launch { sendText() } }) {
			Text("Send!")
		}
	}
}
