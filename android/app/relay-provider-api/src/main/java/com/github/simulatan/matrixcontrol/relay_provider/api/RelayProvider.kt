package com.github.simulatan.matrixcontrol.relay_provider.api

import androidx.compose.runtime.Composable
import kotlin.reflect.KClass

abstract class RelayProvider<S: RelaySettings, R: Relay<S>> {
	abstract val type: KClass<R>
	abstract val settingsClass: KClass<S>
	abstract val name: String
	val qualifier: String = this::class.qualifiedName ?: this::class.simpleName ?: "Unknown"
	abstract val defaultSettings: S

	abstract fun constructRelay(settings: S): R

	fun constructRelayUnsafe(settings: RelaySettings): R {
		@Suppress("UNCHECKED_CAST")
		return constructRelay(settings as S)
	}

	@Composable
	abstract fun Widget(settings: S, callback: (S) -> Unit)
}
