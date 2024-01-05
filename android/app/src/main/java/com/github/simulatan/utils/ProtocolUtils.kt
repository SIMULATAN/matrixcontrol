package com.github.simulatan.utils

import com.github.simulatan.ImmutableMessages
import com.github.simulatan.matrixcontrol.protocol.ProtocolBuilder
import com.github.simulatan.matrixcontrol.protocol.message.MessageBuilder
import com.github.simulatan.matrixcontrol.protocol.message.parts.NewlineMessagePart
import com.github.simulatan.matrixcontrol.protocol.message.parts.PlainTextMessagePart
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess

val client = HttpClient()

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

	sendBytes(protocolBuilder.build().toByteArray(), settings)
}

suspend fun sendBytes(bytes: ByteArray, settings: AppPreferences) {
	val response = client.post("${settings.server}/raw") {
		setBody(bytes)
		header("Serial-Port", settings.serialPort)
	}

	if (!response.status.isSuccess()) {
		val responseText = response.bodyAsText()
		val suffix = if (responseText.isBlank()) {
			""
		} else {
			" - $responseText"
		}
		throw IllegalStateException("Failed to send: Status ${response.status.value}$suffix")
	}
}
