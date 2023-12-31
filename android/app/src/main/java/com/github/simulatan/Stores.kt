package com.github.simulatan

import com.github.simulatan.matrixcontrol.protocol.message.parts.TransitionMessagePart

data class MessageRow(
	val transition: TransitionMessagePart,
	val text: String
) {
	companion object {
		val SAMPLE = MessageRow(transition = TransitionMessagePart.BLITZ, text = "Hello World")
	}
}

typealias Messages = MutableList<MessageRow>