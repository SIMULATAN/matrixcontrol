package com.github.simulatan.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

interface AppPreferences {
	var server: String
	var serialPort: String
	var tabletMode: Boolean
}

data class AppPreferencesContainer(
	override var server: String,
	override var serialPort: String,
	override var tabletMode: Boolean
) : AppPreferences

val MockSettings = AppPreferencesContainer(
	server = "http://localhost:7070",
	serialPort = "/dev/ttyUSB0",
	tabletMode = false
)

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
}
