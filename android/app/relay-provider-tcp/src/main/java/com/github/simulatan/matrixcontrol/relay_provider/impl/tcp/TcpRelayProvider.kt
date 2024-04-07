package com.github.simulatan.matrixcontrol.relay_provider.impl.tcp

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import com.github.simulatan.matrixcontrol.relay.TcpRelay as TcpSender
import com.github.simulatan.matrixcontrol.relay_provider.api.Relay
import com.github.simulatan.matrixcontrol.relay_provider.api.RelayProvider
import com.github.simulatan.matrixcontrol.relay_provider.api.RelaySettings
import com.github.simulatan.matrixcontrol.relay_provider.api.RelayViewModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.logging.Logger

@Serializable
@SerialName("Tcp")
data class TcpRelaySettings(
	val host: String,
	val port: UShort
) : RelaySettings {
	companion object {
		val default = TcpRelaySettings(
			"10.0.2.2",
			2101u
		)
	}
}

open class TcpRelayProvider : RelayProvider<TcpRelaySettings, TcpRelay>() {
	override val type = TcpRelay::class
	override val settingsClass = TcpRelaySettings::class
	override val name = "Tcp"
	override val defaultSettings = TcpRelaySettings.default

	override fun constructRelay(settings: TcpRelaySettings) = TcpRelay(settings)
	@Composable
	override fun Widget(viewModel: RelayViewModel) = TcpWidget(viewModel)
}

@Composable
fun TcpWidget(viewModel: RelayViewModel) {
	val settings = viewModel.getSettings<TcpRelaySettings>()
	TextField(
		value = settings.host,
		onValueChange = {
			viewModel.updateSettings(settings.copy(host = it))
		},
		label = { Text("Host") }
	)

	var portValid by remember { mutableStateOf(true) }
	TextField(
		value =settings.port.toString(),
		onValueChange = {
			val parsed = it.toUShortOrNull()
			portValid = parsed != null
			if (parsed != null)
				viewModel.updateSettings(settings.copy(port = parsed))
		},
		isError = !portValid,
		keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
		label = { Text("Port") }
	)

	if (!portValid) {
		Text(text = "Please enter a valid port!", color = Color.Red)
	}
}

class TcpRelay(settings: TcpRelaySettings) : Relay<TcpRelaySettings> {
	private val logger = Logger.getLogger(javaClass.simpleName)
	private val relay = TcpSender(settings.host, settings.port)

	override suspend fun sendBytes(bytes: UByteArray) {
		if (relay.relayMessage(bytes) == -1) {
			logger.warning("Failed to send message")
		}
	}
}
