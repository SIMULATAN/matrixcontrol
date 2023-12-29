package com.github.simulatan.matrixcontrol.relay

import com.fazecast.jSerialComm.SerialPort

class MatrixRelay(private val port: SerialPort) {
	fun relayMessage(bytes: UByteArray) {
		port.apply {
			baudRate = 2400
			openPort()
			writeBytes(bytes.toByteArray(), bytes.size)
			closePort()
		}
	}

	companion object {
		fun getAvailablePorts(): List<SerialPort> = listOf(*SerialPort.getCommPorts())
	}
}
