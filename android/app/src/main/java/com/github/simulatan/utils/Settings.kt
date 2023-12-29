package com.github.simulatan.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

interface AppPreferences {
	var server: String
	var serialPort: String
}

internal class AppPreferencesImpl(context: Context) : AppPreferences {
	private val dataStore = context.dataStore

	override var server by PreferenceDataStore(
		dataStore = dataStore,
		key = stringPreferencesKey(name = "server"),
		defaultValue = "http://10.0.2.2:7070"
	)

	override var serialPort by PreferenceDataStore(
		dataStore = dataStore,
		key = stringPreferencesKey(name = "serialPort"),
		defaultValue = "/dev/ttyUSB0"
	)
}