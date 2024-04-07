package com.github.simulatan.matrixcontrol.relay_provider.api

interface RelaySettings

data object NoOpRelaySettings : RelaySettings
object NoOpRelay : Relay<NoOpRelaySettings> {
	override suspend fun sendBytes(bytes: UByteArray) = Unit
}
