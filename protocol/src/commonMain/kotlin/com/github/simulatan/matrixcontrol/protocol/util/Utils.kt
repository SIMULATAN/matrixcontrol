@file:OptIn(ExperimentalUnsignedTypes::class)

package com.github.simulatan.matrixcontrol.protocol.util


operator fun UByteArray.plus(other: UByteArray): UByteArray {
	val result = UByteArray(this.size + other.size)
	this.copyInto(result)
	other.copyInto(result, this.size)
	return result
}

operator fun UByteArray.plus(other: ByteArray): UByteArray {
	val result = UByteArray(this.size + other.size)
	this.copyInto(result)
	other.toUByteArray().copyInto(result, this.size)
	return result
}

/**
 * Converts a hex string to a [UByteArray].
 * Same as [kotlin.text.hexToUByteArray] but with spaces between each byte.
 */
fun String.hexToBytes(): UByteArray {
	val hex = this.replace(" ", "")
	return hex.chunked(2).map { it.toUByte(16) }.toUByteArray()
}
