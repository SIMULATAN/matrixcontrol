package com.github.simulatan.matrixcontrol.coolinfoinator.modules

import com.github.simulatan.matrixcontrol.protocol.message.MessageBuilder
import com.github.simulatan.matrixcontrol.protocol.message.parts.PlainTextMessagePart
import kotlinx.datetime.*
import kotlinx.datetime.format.char
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("time")
class TimeModuleConfig() : ModuleConfig()

class TimeModule : Module() {
	override suspend fun contribute(): MessageBuilder {
		return MessageBuilder()
			.add(
				PlainTextMessagePart(
					Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
						.format(LocalDateTime.Format {
							dayOfMonth()
							char('.')
							monthNumber()
							char('.')
							char(' ')
							hour()
							char(':')
							minute()
						})
				)
			)
	}
}
