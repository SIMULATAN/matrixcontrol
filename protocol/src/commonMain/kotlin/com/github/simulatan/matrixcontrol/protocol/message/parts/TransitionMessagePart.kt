package com.github.simulatan.matrixcontrol.protocol.message.parts

import com.github.simulatan.matrixcontrol.protocol.message.MessagePart
import java.util.*

enum class TransitionMessagePart : MessagePart {
	ZYKLISCH,
	STILLSTEHEND,
	VON_RECHTS_OEFFNEN,
	VON_LINKS_OEFFNEN,
	VON_MITTE_OEFFNEN("Von Mitte öffnen"),
	VON_MITTE_OEFFNEN2("Von Mitte öffnen 2"),
	VON_MITTE_UEBERDECKEN("Von Mitte überdecken"),
	VON_RECHTS_UEBERDECKEN,
	VON_LINKS_UEBERDECKEN,
	ZUR_MITTE_UEBERDECKEN("Zur Mitte überdecken"),
	BILDLAUF_NACH_OBEN,
	BILDLAUF_NACH_UNTEN,
	ZEILENSPRUNG_ZUR_MITTE("Zeilensprung zur Mitte"),
	ZEILENSPRUNGBEDECKUNG,
	VON_UNTEN_UEBERDECKEN,
	VON_OBEN_UEBERDECKEN,
	ABTASTLINIE,
	EXPLODIEREN,
	PAC_MAN("Pac Man"),
	FALLENLASSEN_UND_STAPELN,
	SCHIESSEN("Schießen"),
	BLITZ,
	ZUFALL,
	HINEINGLEITEN,
	TEXT_STAUCHEN;

	override fun toByteArray(): UByteArray {
		// transitions are the decimal values 1-25, represented as one byte
		return ubyteArrayOf((ordinal + 1).toUByte())
	}

	val fancyName: String

	constructor() {
		this.fancyName = name
			.lowercase(Locale.ROOT)
			.replace("_", " ")
			.replace("ue", "ü")
			.replace("ae", "ä")
			.replace("oe", "ö")
			.replaceFirstChar { it.uppercase(Locale.ROOT) }
	}

	constructor(fancyName: String) {
		this.fancyName = fancyName
	}
}
