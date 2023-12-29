package com.github.simulatan.matrixcontrol.cli

import com.github.simulatan.matrixcontrol.protocol.ProtocolBuilder
import com.github.simulatan.matrixcontrol.protocol.message.parts.NewlineMessagePart
import com.github.simulatan.matrixcontrol.protocol.message.parts.PlainTextMessagePart
import com.github.simulatan.matrixcontrol.protocol.message.parts.TransitionMessagePart
import com.github.simulatan.matrixcontrol.protocol.util.toHexString
import com.github.simulatan.matrixcontrol.relay.MatrixRelay

fun main() {
	val ports = MatrixRelay.getAvailablePorts()
	println("Available ports:")
	ports.forEachIndexed { index, port ->
		println("- [$index]: ${port.descriptivePortName} (${port.systemPortPath})")
	}
	print("Enter the number of the port you want to use: ")
	val port = ports[readln().toInt()]
	val relay = MatrixRelay(port)
	val protocolBuilder = ProtocolBuilder()
	var input: String
	do {
		print("Please enter a message part (save using 'send', quit using 'exit'): ")
		input = readln()
		if (input == "send") {
			println("Sending message to matrix...")
			val bytes = protocolBuilder.build()
			println("Raw message:\n${bytes.toHexString()}")
			relay.relayMessage(bytes)
			println("Sent!")
			break
		} else if (input == "exit") {
			println("Exiting without saving. (Hint: use 'send' to send the data to the matrix)")
			break
		}

		// when
		// [SOMETHING] => transition
		// \n => Newline (+ new message)
		// anything else => plain text
		protocolBuilder.message += when {
			input.matches("\\[([a-zA-Z_]+)\\]".toRegex()) ->
				TransitionMessagePart.valueOf(input.replace("[", "").replace("]", ""))

			input == "\n" || input == "\\n" -> NewlineMessagePart()

			else -> PlainTextMessagePart(input)
		}
	} while (input != "exit")
}
