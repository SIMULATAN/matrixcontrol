package com.github.simulatan.matrixcontrol.relay

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking

class TCPRelay(host: String, port: UShort) : MatrixRelay {
	private val selectorManager = SelectorManager(Dispatchers.IO)
	private val socket = runBlocking {
		aSocket(selectorManager).tcp().connect(host, port.toInt())
	}
	private val channel = socket.openWriteChannel(autoFlush = true)

	override suspend fun relayMessage(bytes: UByteArray): Int {
		channel.writeByteArray(bytes.toByteArray())

		// ktor doesn't support getting the number of written bytes
		return bytes.size
	}
}
