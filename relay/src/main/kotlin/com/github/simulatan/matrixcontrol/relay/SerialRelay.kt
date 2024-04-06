package com.github.simulatan.matrixcontrol.relay

import com.fazecast.jSerialComm.SerialPort

class SerialRelay(private val port: SerialPort) : MatrixRelay {
	/**
	 * @return number of written bytes or -1 on failure
	 */
	override fun relayMessage(bytes: UByteArray): Int = port.run {
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
