package com.github.simulatan.utils

import com.github.simulatan.ImmutableMessages
import com.github.simulatan.matrixcontrol.protocol.ProtocolBuilder
import com.github.simulatan.matrixcontrol.protocol.message.MessageBuilder
import com.github.simulatan.matrixcontrol.protocol.message.parts.NewlineMessagePart
import com.github.simulatan.matrixcontrol.protocol.message.parts.PlainTextMessagePart

@OptIn(ExperimentalUnsignedTypes::class)
suspend fun send(messages: ImmutableMessages, settings: AppPreferences) {
	val protocolBuilder = ProtocolBuilder()
	val iter = messages.iterator()
	val messageBuilder = MessageBuilder()
	for (item in iter) {
		messageBuilder += item.transition
		messageBuilder += PlainTextMessagePart(item.text)

		if (iter.hasNext()) {
			messageBuilder += NewlineMessagePart
		}
	}

	protocolBuilder.message = messageBuilder

	sendBytes(protocolBuilder.build(), settings)
}

@OptIn(ExperimentalUnsignedTypes::class)
suspend fun sendBytes(bytes: UByteArray, settings: AppPreferences) =
	settings.relay.get()!!
		.sendBytes(bytes)
