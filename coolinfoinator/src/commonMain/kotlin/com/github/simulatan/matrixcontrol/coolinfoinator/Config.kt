package com.github.simulatan.matrixcontrol.coolinfoinator

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import com.github.simulatan.matrixcontrol.coolinfoinator.modules.ModuleConfig
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Serializable
data class Config(
	val communication: CommunicationConfig,
	val modules: List<ModuleConfig>,
	val interval: Duration = 10.seconds,
)

@Serializable
data class CommunicationConfig(
	val host: String,
	val port: UShort,
)

val YAML = Yaml(configuration = YamlConfiguration(
	polymorphismStyle = PolymorphismStyle.Property,
))
