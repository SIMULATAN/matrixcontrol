package com.github.simulatan.matrixcontrol.protocol.message

import com.github.simulatan.matrixcontrol.protocol.message.parts.NewlineMessagePart
import com.github.simulatan.matrixcontrol.protocol.message.parts.PlainTextMessagePart
import com.github.simulatan.matrixcontrol.protocol.message.parts.TransitionMessagePart
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals

class TransitionTestCases {
	@Test
	fun `#7 two lines same transition`() {
		val message = MessageBuilder(
			TransitionMessagePart.PAC_MAN,
			PlainTextMessagePart("First Line"),
			NewlineMessagePart,
			TransitionMessagePart.PAC_MAN,
			PlainTextMessagePart("Second Line")
		).build()
		// Transition + Start of line + "First Line" + Newline + Message Terminator + Transition + Start of line + "Second Line" + Newline + Message Terminator
		val expected = "13 EF B0 EF A2 46 69 72 73 74 20 4C 69 6E 65 EF B1 EF A0 FF 13 EF B0 EF A2 53 65 63 6F 6E 64 20 4C 69 6E 65 FF"
			.replace(" ", "")
			.hexToUByteArray()
		assertContentEquals(expected, message)
	}

	@Test
	fun `#9 three lines pac man`() {
		val message = MessageBuilder(
			TransitionMessagePart.PAC_MAN,
			PlainTextMessagePart("First Line"),
			NewlineMessagePart,
			TransitionMessagePart.PAC_MAN,
			PlainTextMessagePart("Second Line"),
			NewlineMessagePart,
			TransitionMessagePart.PAC_MAN,
			PlainTextMessagePart("Third Line")
		).build()
		val expected = "13 EF B0 EF A2 46 69 72 73 74 20 4C 69 6E 65 EF B1 EF A0 FF 13 EF B0 EF A2 53 65 63 6F 6E 64 20 4C 69 6E 65 EF B1 EF A0 FF 13 EF B0 EF A2 54 68 69 72 64 20 4C 69 6E 65 FF"
			.replace(" ", "")
			.hexToUByteArray()
		assertContentEquals(expected, message)
	}
}
