package com.github.simulatan.matrixcontrol.coolinfoinator.modules

import com.github.simulatan.matrixcontrol.protocol.message.MessageBuilder

sealed class Module {
	abstract suspend fun contribute(): MessageBuilder
}
