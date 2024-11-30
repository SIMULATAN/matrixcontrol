package com.github.simulatan.matrixcontrol.relay.server

import com.github.simulatan.matrixcontrol.relay.SerialRelay
import io.ktor.http.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
	embeddedServer(CIO, module = {
		routing {
			post("/raw") {
				val requestedPort = call.request.header("Serial-Port")
					?: throw IllegalArgumentException("Serial-Port header missing")
				val availablePorts = SerialRelay.getAvailablePorts()
				val port = availablePorts.find { it.systemPortPath == requestedPort } ?: throw IllegalArgumentException("Serial port $requestedPort not found")

				val relay = SerialRelay(port)

				val bytes = call.receive<ByteArray>()
				println("Relaying ${bytes.size} bytes...")
				val writtenBytes = relay.relayMessage(bytes.toUByteArray())
				println("Relayed $writtenBytes bytes!")

				if (writtenBytes == bytes.size) {
					call.respond(HttpStatusCode.NoContent)
				} else {
					call.respondText(
						when (writtenBytes) {
							-1 -> "Couldn't write anything. Are the permissions correct?"
							else -> "Couldn't write all bytes. Only $writtenBytes of ${bytes.size} bytes were written."
						},
						status = HttpStatusCode.InternalServerError
					)
				}
			}
		}
	}).start(wait = true)
}
