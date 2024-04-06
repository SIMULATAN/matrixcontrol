package com.github.simulatan.matrixcontrol.relay

import java.net.Socket

/**
 * Sends the given bytes to the matrix display over reverse TCP.
 */
class TcpRelay(host: String, port: Short) : MatrixRelay {
	private val socket = Socket(host, port.toInt())

	override fun relayMessage(bytes: UByteArray): Int {
		val outputStream = socket.getOutputStream()
		outputStream.write(bytes.toByteArray())
		outputStream.flush()
		outputStream.close()
		socket.close()
		return bytes.size
	}
}
