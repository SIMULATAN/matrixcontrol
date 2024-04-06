package com.github.simulatan.matrixcontrol.relay_provider.api

interface Relay<S : RelaySettings> {
	suspend fun sendBytes(bytes: ByteArray)
}
