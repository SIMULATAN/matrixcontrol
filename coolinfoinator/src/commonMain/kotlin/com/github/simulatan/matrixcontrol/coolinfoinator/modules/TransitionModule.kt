package com.github.simulatan.matrixcontrol.coolinfoinator.modules

import com.github.simulatan.matrixcontrol.protocol.message.MessageBuilder
import com.github.simulatan.matrixcontrol.protocol.message.parts.NewlineMessagePart
import com.github.simulatan.matrixcontrol.protocol.message.parts.TransitionMessagePart
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("transition")
data class TransitionModuleConfig(
	val transition: TransitionMessagePart = TransitionMessagePart.STILLSTEHEND
) : ModuleConfig()

class TransitionModule(config: TransitionModuleConfig) : Module() {
	val transition = config.transition

	override suspend fun contribute(): MessageBuilder {
		return MessageBuilder(NewlineMessagePart, transition)
	}
}
