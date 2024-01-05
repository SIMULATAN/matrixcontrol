package com.github.simulatan.matrixcontrol.relay

import com.fazecast.jSerialComm.SerialPort

class MatrixRelay(private val port: SerialPort) {
	/**
	 * @return number of written bytes or -1 on failure
	 */
	fun relayMessage(bytes: UByteArray): Int = port.run {
		baudRate = 2400
		openPort()
		val writtenBytes = writeBytes(bytes.toByteArray(), bytes.size)
		closePort()
		return writtenBytes
	}

	companion object {
		fun getAvailablePorts(): List<SerialPort> = listOf(*SerialPort.getCommPorts())
	}
}
