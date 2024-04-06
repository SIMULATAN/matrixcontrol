package com.github.simulatan.utils

import com.github.simulatan.matrixcontrol.relay_provider.api.Relay
import com.github.simulatan.matrixcontrol.relay_provider.api.RelayProvider
import com.github.simulatan.matrixcontrol.relay_provider.api.RelaySettings
import kotlinx.serialization.Serializable

@Serializable
data class PickedRelay(
	val type: String,
	var settings: RelaySettings
) {
	init {
		if (type.isBlank()) throw IllegalArgumentException("type must not be blank")
	}

	fun getProvider() = RelayRegistry.getRelayProvider(type)
	fun get() = getProvider()?.constructRelayUnsafe(settings)

	companion object {
		fun <S : RelaySettings> fromProvider(provider: RelayProvider<S, out Relay<S>>)
			= PickedRelay(type = provider.qualifier, settings = provider.defaultSettings)

		fun <S : RelaySettings> fromRelay(relay: Relay<S>, settings: S)
			= PickedRelay(type = relay::class.qualifiedName!!, settings = settings)
	}
}
