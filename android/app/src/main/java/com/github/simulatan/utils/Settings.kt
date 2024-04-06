package com.github.simulatan.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.github.simulatan.matrixcontrol.relay_provider.api.NoOpRelay
import com.github.simulatan.matrixcontrol.relay_provider.api.NoOpRelaySettings
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.plus
import kotlin.reflect.typeOf


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


interface AppPreferences {
	var server: String
	var serialPort: String
	var tabletMode: Boolean
	var presets_instantApply: Boolean
	var presets_stayOnPage: Boolean
	var relay: PickedRelay
}

val noOpRelay = PickedRelay.fromRelay(NoOpRelay, NoOpRelaySettings)

data class AppPreferencesContainer(
	override var server: String,
	override var serialPort: String,
	override var tabletMode: Boolean,
	override var presets_instantApply: Boolean = true,
	override var presets_stayOnPage: Boolean = false,
	override var relay: PickedRelay = noOpRelay
) : AppPreferences

val DefaultSettings = AppPreferencesContainer(
	server = "http://localhost:7070",
	serialPort = "/dev/ttyUSB0",
	tabletMode = false
)

object MockSettings {
	val TabletMode = DefaultSettings.copy(tabletMode = true)
	val NoTabletMode = DefaultSettings.copy(tabletMode = false)
}

internal class AppPreferencesImpl(context: Context) : AppPreferences {
	private val dataStore = context.dataStore

	override var server by PreferenceDataStore(
		dataStore = dataStore,
		key = stringPreferencesKey("server"),
		defaultValue = "http://10.0.2.2:7070"
	)

	override var serialPort by PreferenceDataStore(
		dataStore = dataStore,
		key = stringPreferencesKey("serialPort"),
		defaultValue = "/dev/ttyUSB0"
	)

	override var tabletMode by PreferenceDataStore(
		dataStore = dataStore,
		key = booleanPreferencesKey("tabletMode"),
		defaultValue = false
	)

	override var presets_instantApply by PreferenceDataStore(
		dataStore = dataStore,
		key = booleanPreferencesKey("presets.instantApply"),
		defaultValue = true
	)

	override var presets_stayOnPage by PreferenceDataStore(
		dataStore = dataStore,
		key = booleanPreferencesKey("presets.stayOnPage"),
		defaultValue = false
	)

	override var relay by JsonPreferencesDataStore(
		dataStore = dataStore,
		name = "relay",
		clazz = typeOf<PickedRelay>(),
		defaultValue = noOpRelay,
		module = Json {
			serializersModule += RelayRegistry.module
		}
	)
}
