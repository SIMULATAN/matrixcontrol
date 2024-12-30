package com.github.simulatan.matrixcontrol.coolinfoinator

import com.github.simulatan.matrixcontrol.coolinfoinator.modules.Module
import com.github.simulatan.matrixcontrol.coolinfoinator.modules.createModule
import com.github.simulatan.matrixcontrol.protocol.ProtocolBuilder
import com.github.simulatan.matrixcontrol.relay.MatrixRelay
import com.github.simulatan.matrixcontrol.relay.TCPRelay
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import kotlin.time.Duration.Companion.seconds

fun main() = runBlocking {
	val configSource = FileSystem.SYSTEM.source("config.yml".toPath())
	val config = YAML.decodeFromSource<Config>(configSource)

	val modules = config.modules.map { it.createModule() }

	println("Starting connection loop")
	runConnectionLoop(config, modules)
}

suspend fun runConnectionLoop(config: Config, modules: Collection<Module>) {
	while (true) {
		runCatching {
			println("Connecting to ${config.communication.host}:${config.communication.port}")
			val relay = TCPRelay(config.communication.host, config.communication.port)
			println("Connection established")

			runSendLoop(config, modules, relay)
		}.onFailure {
			it.printStackTrace()
		}
		delay(10.seconds)
	}
}

suspend fun runSendLoop(config: Config, modules: Collection<Module>, relay: MatrixRelay) {
	while (true) {
		println("Accumulating messages")
		val message = modules
			.map { it.contribute() }
			.reduce { acc, message -> acc.add(message) }

		val protocol = ProtocolBuilder()
		protocol.message = message

		println("Updating content")
		relay.relayMessage(protocol.build())

		delay(config.interval)
	}
}
