package com.github.simulatan.matrixcontrol.coolinfoinator.modules

import com.github.simulatan.matrixcontrol.protocol.message.MessageBuilder
import com.github.simulatan.matrixcontrol.protocol.message.parts.PlainTextMessagePart
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.simulatan.uwz.getWarningLevel
import me.simulatan.uwz.polygon.Point

@Serializable
@SerialName("uwz")
data class UwzConfig(
	val location: Point,
) : ModuleConfig()

class UwzModule(config: UwzConfig) : Module() {
	val location: Point = config.location

	override suspend fun contribute(): MessageBuilder {
		val warningLevel = getWarningLevel(location)

		return MessageBuilder(PlainTextMessagePart(warningLevel))
	}
}
