package com.github.simulatan.utils

import androidx.annotation.WorkerThread
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KType

fun <T> DataStore<Preferences>.get(
	key: Preferences.Key<T>
): T? = runBlocking {
	data.first()[key]
}

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

open class PreferenceDataStore<T>(
	private val dataStore: DataStore<Preferences>,
	internal val key: Preferences.Key<T>,
	private val defaultValue: T
) : ReadWriteProperty<Any, T> {
	@WorkerThread
	override fun getValue(thisRef: Any, property: KProperty<*>)
		= dataStore.get(key = key, defaultValue = defaultValue)

	override fun setValue(thisRef: Any, property: KProperty<*>, value: T)
		= dataStore.set(key = key, value = value)
}

open class JsonPreferencesDataStore<T : Any>(
	private val dataStore: DataStore<Preferences>,
	name: String,
	private val defaultValue: T,
	clazz: KType,
	private val module: Json = Json.Default
) : ReadWriteProperty<Any, T> {
	private val key = stringPreferencesKey(name)
	@Suppress("UNCHECKED_CAST")
	private val serializer: KSerializer<T> = serializer(clazz) as KSerializer<T>

	@WorkerThread
	override fun getValue(thisRef: Any, property: KProperty<*>): T = dataStore.get(key = key, defaultValue = "<null>").let {
		when (it) {
			"<null>" -> null
			else -> it
		}
	}?.let { module.decodeFromString(serializer, it) } ?: defaultValue

	override fun setValue(thisRef: Any, property: KProperty<*>, value: T)
		= dataStore.set(key = key, value = module.encodeToString(serializer, value))
}
