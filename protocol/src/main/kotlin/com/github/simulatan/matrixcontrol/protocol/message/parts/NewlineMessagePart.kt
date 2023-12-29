package com.github.simulatan.matrixcontrol.protocol.message.parts

import com.github.simulatan.matrixcontrol.protocol.message.MessagePart
import com.github.simulatan.matrixcontrol.protocol.util.hexToBytes

class NewlineMessagePart : MessagePart {
	override fun toByteArray(): UByteArray = "EF B1 EF A0".hexToBytes()
}
