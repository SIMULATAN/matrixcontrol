package com.github.simulatan.matrixcontrol.relay

interface MatrixRelay {
	/**
	 * @return number of written bytes or -1 on failure
	 */
	suspend fun relayMessage(bytes: UByteArray): Int
}
