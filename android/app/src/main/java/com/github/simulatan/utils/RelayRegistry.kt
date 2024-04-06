package com.github.simulatan.utils

import com.github.simulatan.matrixcontrol.relay_provider.api.RelayProvider
import com.github.simulatan.matrixcontrol.relay_provider.api.RelaySettings
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import java.util.ServiceLoader
import kotlin.reflect.KClass

@OptIn(InternalSerializationApi::class)
object RelayRegistry {
	private val relays = ServiceLoader.load(RelayProvider::class.java)
		.associateBy {
			it.qualifier
		}
	val module = SerializersModule {
		polymorphic(RelaySettings::class) {
			relays.forEach { (_, provider) ->
				fun <T: RelaySettings> serializer(clazz: KClass<T>, serializer: KSerializer<*>) {
					@Suppress("UNCHECKED_CAST")
					subclass(clazz, serializer as KSerializer<T>)
				}
				val serializer = provider.settingsClass.serializer()
				serializer(provider.settingsClass, serializer)
			}
		}
	}

	fun getRelayProvider(type: String)
		= relays[type]

	fun getAvailableRelays() = relays.values
}
