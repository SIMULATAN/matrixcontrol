package com.github.simulatan.matrixcontrol.relay_provider.impl.ktor

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.simulatan.matrixcontrol.relay_provider.api.Relay
import com.github.simulatan.matrixcontrol.relay_provider.api.RelayProvider
import com.github.simulatan.matrixcontrol.relay_provider.api.RelaySettings
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.logging.Logger

@Serializable
@SerialName("Ktor")
data class KtorRelaySettings(
	val server: String,
	val serialPort: String
) : RelaySettings {
	companion object {
		val default = KtorRelaySettings(
			"http://10.0.2.2:7070",
			"/dev/ttyUSB0"
		)
	}
}

class KtorRelayProvider : RelayProvider<KtorRelaySettings, KtorRelay>() {
	override val type = KtorRelay::class
	override val settingsClass = KtorRelaySettings::class
	override val name = "Ktor"
	override val defaultSettings = KtorRelaySettings.default

	override fun constructRelay(settings: KtorRelaySettings) = KtorRelay(settings)
	@Composable
	override fun Widget(settings: KtorRelaySettings, settingsCallback: (KtorRelaySettings) -> Unit) {
		var server by remember { mutableStateOf(settings.server) }
		TextField(
			value = server,
			onValueChange = {
				server = it
				settingsCallback(settings.copy(server = it))
			},
			label = { Text("Server") })

		var serialPort by remember { mutableStateOf(settings.serialPort) }
		TextField(
			value = serialPort,
			onValueChange = {
				serialPort = it
				settingsCallback(settings.copy(serialPort = it))
			},
			label = { Text("Serial Port") }
		)
	}
}

class KtorRelay(val settings: KtorRelaySettings) : Relay<KtorRelaySettings> {
	private val logger = Logger.getLogger(javaClass.simpleName)
	private val client = HttpClient()

	override suspend fun sendBytes(bytes: ByteArray) {
		val response = client.post("${settings.server}/raw") {
			setBody(bytes)
			header("Serial-Port", settings.serialPort)
		}

		if (!response.status.isSuccess()) {
			val responseText = response.bodyAsText()
			val suffix = if (responseText.isBlank()) {
				""
			} else {
				" - $responseText"
			}
			throw IllegalStateException("Failed to send: Status ${response.status.value}$suffix")
		}
	}
}
