package com.github.simulatan.matrixcontrol.relay_provider.impl.digionesp

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.digi.addp.client.AddpClient
import com.digi.addp.devices.Device
import com.github.simulatan.matrixcontrol.relay_provider.api.RelayViewModel
import com.github.simulatan.matrixcontrol.relay_provider.impl.tcp.TcpRelayProvider
import com.github.simulatan.matrixcontrol.relay_provider.impl.tcp.TcpRelaySettings
import com.github.simulatan.matrixcontrol.relay_provider.impl.tcp.TcpWidget
import java.util.logging.Logger

class DigiOneSPRelayProvider : TcpRelayProvider() {
	override val name = "DigiOneSP"

	init {
		AddpClient.init()
	}

	@Composable
	override fun Widget(viewModel: RelayViewModel) {
		TcpWidget(viewModel)
		Button(onClick = {
			DigiOneSPScanManager.scan {
				viewModel.updateSettings(viewModel.getSettings<TcpRelaySettings>().copy(
					host = it.ipAddress.toString(),
					// default "raw" port - make sure to change if configured
					port = 2101u
				))
			}
		}) {
			Text(text = "Autoconfigure")
		}
	}
}

/**
 * Encapsulates logic to avoid scanning for DigiOneSP devices multiple times.
 */
object DigiOneSPScanManager {
	private var lastScan = -1
	private val logger = Logger.getLogger(DigiOneSPScanManager::class.qualifiedName!!)

	fun cancelLastScan() {
		if (lastScan != -1) {
			logger.info("Stopping previous ADDP search")
			AddpClient.ADDPStopSearch(lastScan)
			lastScan = -1
		}
	}

	fun scan(callback: (Device) -> Unit) {
		cancelLastScan()
		logger.info("Starting ADDP search")
		lastScan = AddpClient.ADDPStartAsyncSearchCallback {
			cancelLastScan()
			logger.info("Found ADDP device: $it")
			callback(it)
		}
	}
}
