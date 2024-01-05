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
			println("Relaying ${bytes.size} bytes...")
			val writtenBytes = relay.relayMessage(bytes.toUByteArray())
			println("Relayed $writtenBytes bytes!")
			if (writtenBytes == bytes.size) {
				ctx.status(HttpStatus.NO_CONTENT)
			} else {
				ctx.status(HttpStatus.INTERNAL_SERVER_ERROR)
				if (writtenBytes == -1) {
					ctx.result("Couldn't write anything. Are the permissions correct?")
				} else {
					ctx.result("Couldn't write all bytes. Only $writtenBytes of ${bytes.size} bytes were written.")
				}
			}
		}.start(7070)
}
