package com.github.simulatan.matrixcontrol.relay_provider.api

import kotlinx.serialization.Serializable

@Serializable
data class PickedRelay(
	val type: String,
	val settings: RelaySettings
) {
	init {
		if (type.isBlank()) throw IllegalArgumentException("type must not be blank")
	}

	companion object {
		fun <S : RelaySettings> fromProvider(provider: RelayProvider<S, out Relay<S>>) = PickedRelay(type = provider.qualifier, settings = provider.defaultSettings)

		fun <S : RelaySettings> fromRelay(relay: Relay<S>, settings: S) = PickedRelay(type = relay::class.qualifiedName!!, settings = settings)
	}
}
