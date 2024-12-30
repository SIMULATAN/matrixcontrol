package com.github.simulatan.matrixcontrol.protocol.message

import com.github.simulatan.matrixcontrol.protocol.message.parts.PlainTextMessagePart
import kotlin.test.Test
import kotlin.test.assertContentEquals

class SimpleTestCases {
	@Test
	fun `1 single line`() {
		val message = MessageBuilder(
			PlainTextMessagePart("HALLO")
		).build()
		// Start of line + "Hello World" + Message Terminator
		val expected = "EF B0 EF A2 48 41 4C 4C 4F FF"
			.replace(" ", "")
			.hexToUByteArray()
		assertContentEquals(expected, message)
	}

	@Test
	fun `2 single line`() {
		val message = MessageBuilder(
			PlainTextMessagePart("TEST")
		).build()
		// Start of line + "Hello World" + Message Terminator
		val expected = "EF B0 EF A2 54 45 53 54 FF"
			.replace(" ", "")
			.hexToUByteArray()
		assertContentEquals(expected, message)
	}
}
