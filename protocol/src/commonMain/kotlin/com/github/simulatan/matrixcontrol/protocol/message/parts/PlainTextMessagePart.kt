package com.github.simulatan.matrixcontrol.protocol.message.parts

import com.github.simulatan.matrixcontrol.protocol.message.MessagePart

class PlainTextMessagePart(private val message: String) : MessagePart {
	override fun toByteArray(): UByteArray = message.encodeToByteArray().toUByteArray()
}
