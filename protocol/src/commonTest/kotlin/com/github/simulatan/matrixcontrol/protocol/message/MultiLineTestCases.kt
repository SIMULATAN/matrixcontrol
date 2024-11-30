package com.github.simulatan.matrixcontrol.protocol.message

import com.github.simulatan.matrixcontrol.protocol.message.parts.NewlineMessagePart
import com.github.simulatan.matrixcontrol.protocol.message.parts.PlainTextMessagePart
import kotlin.test.Test
import kotlin.test.assertContentEquals

class MultiLineTestCases {
	@Test
	fun `#6 two lines`() {
		val message = MessageBuilder(
			PlainTextMessagePart("First Line"),
			NewlineMessagePart,
			PlainTextMessagePart("Second Line")
		).build()
		// Start of line + "First Line" + Newline + Message Terminator + Start of line + "Second Line" + Newline + Message Terminator
		val expected = "EF B0 EF A2 46 69 72 73 74 20 4C 69 6E 65 EF B1 EF A0 FF EF B0 EF A2 53 65 63 6F 6E 64 20 4C 69 6E 65 FF"
			.replace(" ", "")
			.hexToUByteArray()
		assertContentEquals(expected, message)
	}

	@Test
	fun `#8 three lines`() {
		val message = MessageBuilder(
			PlainTextMessagePart("First Line"),
			NewlineMessagePart,
			PlainTextMessagePart("Second Line"),
			NewlineMessagePart,
			PlainTextMessagePart("Third Line")
		).build()
		// Start of line + "First Line" + Newline + Message Terminator + Start of line + "Second Line" + Newline + Message Terminator + Start of line + "Third Line" + Newline + Message Terminator
		val expected = "EF B0 EF A2 46 69 72 73 74 20 4C 69 6E 65 EF B1 EF A0 FF EF B0 EF A2 53 65 63 6F 6E 64 20 4C 69 6E 65 EF B1 EF A0 FF EF B0 EF A2 54 68 69 72 64 20 4C 69 6E 65 FF"
			.replace(" ", "")
			.hexToUByteArray()
		assertContentEquals(expected, message)
	}
}
