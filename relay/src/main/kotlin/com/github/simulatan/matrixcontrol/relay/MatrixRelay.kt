package com.github.simulatan.matrixcontrol.relay

interface MatrixRelay {
	/**
	 * @return number of written bytes or -1 on failure
	 */
	fun relayMessage(bytes: UByteArray): Int
}
