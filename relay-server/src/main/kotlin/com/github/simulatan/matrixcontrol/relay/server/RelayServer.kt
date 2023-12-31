package com.github.simulatan.matrixcontrol.relay.server

import com.github.simulatan.matrixcontrol.relay.MatrixRelay
import io.javalin.Javalin
import io.javalin.http.HttpStatus

fun main() {
	val app = Javalin.create()
		.post("/raw") { ctx ->
			val portHeader = ctx.header("Serial-Port") ?: throw IllegalArgumentException("Serial-Port header missing")
			val availablePorts = MatrixRelay.getAvailablePorts()
			val port = availablePorts.find { it.systemPortPath == portHeader } ?: throw IllegalArgumentException("Serial port $portHeader not found")
			val relay = MatrixRelay(port)
			val bytes = ctx.bodyAsBytes()
			println("Relaying ${bytes.size} bytes")
			relay.relayMessage(bytes.toUByteArray())
			ctx.status(HttpStatus.NO_CONTENT)
		}.start(7070)
}
