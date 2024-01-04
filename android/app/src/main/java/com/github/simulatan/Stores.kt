package com.github.simulatan

import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.github.simulatan.matrixcontrol.protocol.message.parts.TransitionMessagePart
import com.github.simulatan.utils.get
import com.github.simulatan.utils.set
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class MessageRow(
	val transition: TransitionMessagePart,
	val text: String
) {
	companion object {
		val SAMPLE = MessageRow(transition = TransitionMessagePart.BLITZ, text = "Hello World")
		val SAMPLE2 = MessageRow(transition = TransitionMessagePart.PAC_MAN, text = "Second")
	}
}

typealias Messages = MutableList<MessageRow>
typealias ImmutableMessages = List<MessageRow>

@Serializable
data class Preset(
	val name: String,
	val messageRows: ImmutableMessages
) {
	companion object {
		val SAMPLE = Preset(
			name = "Sample",
			messageRows = listOf(MessageRow.SAMPLE, MessageRow.SAMPLE2)
		)
		val SAMPLE2 = Preset(
			name = "My other sample",
			messageRows = listOf(MessageRow.SAMPLE2, MessageRow.SAMPLE, MessageRow.SAMPLE2)
		)
	}
}

typealias Presets = MutableList<Preset>
typealias ImmutablePresets = List<Preset>

inline fun <reified T> Bundle?.getObject(key: String, default: T): T =
	this?.getString(key)?.let { Json.decodeFromString<T>(it) }
		?: default

inline fun <reified T> Bundle.putObject(key: String, value: T)
	= putString(key, Json.encodeToString<T>(value))

val presetsKey = stringPreferencesKey("presets")

inline fun <reified T> DataStore<Preferences>.setObject(key: Preferences.Key<String>, value: T) =
	this.set(
		key,
		Json.encodeToString(value)
	)

inline fun <reified T> DataStore<Preferences>.getObject(key: Preferences.Key<String>): T? =
	this.get(key)?.let { Json.decodeFromString<T>(it) }
