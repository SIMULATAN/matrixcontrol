package com.github.simulatan.matrixcontrol.protocol

import com.github.simulatan.matrixcontrol.protocol.message.MessageBuilder
import com.github.simulatan.matrixcontrol.protocol.message.parts.PlainTextMessagePart
import com.github.simulatan.matrixcontrol.protocol.util.plus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

class ProtocolBuilder {

	var message: MessageBuilder = MessageBuilder()

	/**
	 * a function that returns the current time
	 * this is used to generate the timestamp in the protocol
	 * which in turn is (probably) used to display the current time on the matrix
	 */
	var timeProvider: () -> TemporalAccessor = { LocalDateTime.now() }

	fun build(): UByteArray {
		return "00FFFF010B01FF1050FF080400".hexToUByteArray() +
			DATE_TIME_FORMATTER.format(timeProvider()).toByteArray() +
			"FF013031".hexToUByteArray() +
			message.build() +
			"FF02300030303030323335393031FF040000FF0500FF06303030303030FF07303030303030FF00".hexToUByteArray()
	}

	companion object {
		val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyMMddHHmmss")

		fun from(builder: ProtocolBuilder.() -> Unit): ProtocolBuilder {
			return ProtocolBuilder().apply(builder)
		}

		fun of(builder: ProtocolBuilder.() -> Unit): UByteArray {
			return from(builder).build()
		}

		fun of(message: String): UByteArray {
			return of { this.message += PlainTextMessagePart(message) }
		}
	}
}
