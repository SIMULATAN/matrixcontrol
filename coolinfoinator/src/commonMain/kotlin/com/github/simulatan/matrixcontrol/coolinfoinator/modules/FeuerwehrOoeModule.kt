package com.github.simulatan.matrixcontrol.coolinfoinator.modules

import com.github.simulatan.matrixcontrol.protocol.message.MessageBuilder
import com.github.simulatan.matrixcontrol.protocol.message.parts.PlainTextMessagePart
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
@SerialName("feuerwehr_ooe")
data class FeuerwehrOoeConfig(
	val displayType: FeuerwehrOoeDisplayType,
) : ModuleConfig()

@Serializable
sealed interface FeuerwehrOoeDisplayType {
	@Serializable
	@SerialName("involved_l")
	data class InvolvedLocal(val trackedFwnr: Int) : FeuerwehrOoeDisplayType

	@Serializable
	@SerialName("count_l")
	data class CountLocal(val trackedFwnr: Int) : FeuerwehrOoeDisplayType

	@Serializable
	@SerialName("count")
	object Count : FeuerwehrOoeDisplayType
}

class FeuerwehrOoeModule(private val config: FeuerwehrOoeConfig) : Module() {
	private val client = HttpClient {
		install(ContentNegotiation) {
			// thank you, LFV
			json(Json {
				ignoreUnknownKeys = true
			}, contentType = ContentType.Text.Plain)
		}
	}

	@Serializable
	data class EinsaetzeResponse(
		// is null when no einsaetze are present
		val einsaetze: Map<String, EinsatzWrapper>? = null,
		@SerialName("cnt_einsaetze")
		val einsatzeNr: Int,
	)

	@Serializable
	data class EinsatzWrapper(
		val einsatz: Einsatz,
	)

	@Serializable
	data class Einsatz(
		@SerialName("feuerwehrenarray")
		val feuerwehren: Map<String, Feuerwehr>,
	)

	@Serializable
	data class Feuerwehr(
		@SerialName("fwnr")
		val nr: Int,
		@SerialName("fwname")
		val name: String,
	)

	override suspend fun contribute(): MessageBuilder {
		val response = client.get("https://cf-einsaetze.ooelfv.at/webext2/rss/json_laufend.txt")
			.body<EinsaetzeResponse>()

		var einsaetze = response.einsaetze
			?.values
			?.map(EinsatzWrapper::einsatz)
			.orEmpty()

		val text = when (val display = config.displayType) {
			FeuerwehrOoeDisplayType.Count -> "B ${response.einsatzeNr} Einsaetze"
			is FeuerwehrOoeDisplayType.CountLocal -> {
				val count = einsaetze
					.count { it.feuerwehren.values.any { it.nr == display.trackedFwnr } }

				"L $count Einsaetze"
			}
			is FeuerwehrOoeDisplayType.InvolvedLocal -> {
				val feuerwehr = einsaetze
					.find { it.feuerwehren.values.any { it.nr == display.trackedFwnr } }

				feuerwehr?.feuerwehren
					?.values
					?.joinToString { it.name }
					?.let { "L $it" }
					?: "L 0 Einsaetze"
			}
		}

		return MessageBuilder(PlainTextMessagePart(text))
	}
}
