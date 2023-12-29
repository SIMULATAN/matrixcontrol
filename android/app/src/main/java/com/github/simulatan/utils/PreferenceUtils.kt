package com.github.simulatan.utils

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


fun <T> DataStore<Preferences>.get(
	key: Preferences.Key<T>,
	defaultValue: T
): T = runBlocking {
	data.first()[key] ?: defaultValue
}

fun <T> DataStore<Preferences>.set(
	key: Preferences.Key<T>,
	value: T?
) = runBlocking<Unit> {
	edit {
		if (value == null) {
			it.remove(key)
		} else {
			it[key] = value
		}
	}
}

class PreferenceDataStore<T>(
	private val dataStore: DataStore<Preferences>,
	private val key: Preferences.Key<T>,
	private val defaultValue: T
) : ReadWriteProperty<Any, T> {
	@WorkerThread
	override fun getValue(thisRef: Any, property: KProperty<*>)
		= dataStore.get(key = key, defaultValue = defaultValue)

	override fun setValue(thisRef: Any, property: KProperty<*>, value: T)
		= dataStore.set(key = key, value = value)
}