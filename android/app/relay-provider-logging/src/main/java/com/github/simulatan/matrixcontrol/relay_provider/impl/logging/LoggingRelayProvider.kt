package com.github.simulatan.matrixcontrol.relay_provider.impl.logging

import androidx.compose.runtime.Composable
import com.github.simulatan.matrixcontrol.relay_provider.api.Relay
import com.github.simulatan.matrixcontrol.relay_provider.api.RelayProvider
import com.github.simulatan.matrixcontrol.relay_provider.api.RelaySettings
import com.github.simulatan.matrixcontrol.relay_provider.api.RelayViewModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.logging.Level
import java.util.logging.Logger

@Serializable
@SerialName("Logging")
object LoggingSettings : RelaySettings

class LoggingRelayProvider : RelayProvider<LoggingSettings, LoggingRelay>() {
	override val type = LoggingRelay::class
	override val settingsClass = LoggingSettings::class
	override val name = "Logging"
	override val defaultSettings = LoggingSettings

	override fun constructRelay(settings: LoggingSettings) = LoggingRelay()
	@Composable
	override fun Widget(viewModel: RelayViewModel) = Unit
}

@OptIn(ExperimentalUnsignedTypes::class)
class LoggingRelay : Relay<LoggingSettings> {
	private val logger = Logger.getLogger(javaClass.simpleName)
	override suspend fun sendBytes(bytes: UByteArray) {
		logger.log(Level.INFO, bytes.toString())
	}
}
