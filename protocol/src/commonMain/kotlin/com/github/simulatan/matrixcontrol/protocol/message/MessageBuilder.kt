package com.github.simulatan.matrixcontrol.protocol.message

import com.github.simulatan.matrixcontrol.protocol.message.parts.NewlineMessagePart
import com.github.simulatan.matrixcontrol.protocol.message.parts.TransitionMessagePart
import com.github.simulatan.matrixcontrol.protocol.util.hexToBytes
import com.github.simulatan.matrixcontrol.protocol.util.plus

private val START_OF_LINE = "EF B0 EF A2".hexToBytes()
private const val MESSAGE_TERMINATOR: UByte = 255u

class MessageBuilder {
	private val parts: MutableList<MessagePart>

	constructor() {
		parts = mutableListOf()
	}

	constructor(messages: Collection<MessagePart>) {
		parts = messages.toMutableList()
	}

	constructor(vararg messages: MessagePart): this(messages.toList())

	constructor(messages: MessageBuilder.() -> Unit): this() {
		messages()
	}

	fun add(vararg messagePart: MessagePart): MessageBuilder {
		parts += messagePart
		return this
	}

	fun add(messageParts: Collection<MessagePart>): MessageBuilder {
		parts += messageParts
		return this
	}

	fun add(messageBuilder: MessageBuilder): MessageBuilder {
		parts += messageBuilder.parts
		return this
	}

	operator fun plusAssign(messagePart: MessagePart) {
		add(messagePart)
	}

	fun build(): UByteArray {
		if (parts.isEmpty()) {
			return ubyteArrayOf()
		}

		var result: UByteArray
		var skipNext = false
		// transitions are before the START_OF_LINE control sequence
		if (parts[0] is TransitionMessagePart) {
			result = parts[0].toByteArray() + START_OF_LINE
			skipNext = true
		} else {
			result = START_OF_LINE
		}

		// useful to skip a certain message part due to for ex. lookaheads
		for (i in 0 until parts.size) {
			if (skipNext) {
				skipNext = false
				continue
			}

			val part = parts[i]
			val next = if (i + 1 < parts.size) parts[i + 1] else null

			result += part.toByteArray()
			if (part is NewlineMessagePart || next == null) {
				// "message terminator" control sequence
				result += MESSAGE_TERMINATOR

				if (next is TransitionMessagePart) {
					// transition control sequence has to be inserted before the start of line
					result += next.toByteArray()
					skipNext = true
				}
				if (next != null) {
					// start of line control sequence has to be inserted before the next message
					result += START_OF_LINE
				}
			}
		}
		return result
	}
}
