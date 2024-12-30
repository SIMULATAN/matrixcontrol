package com.github.simulatan.matrixcontrol.coolinfoinator.modules

import kotlinx.serialization.Serializable

@Serializable
sealed class ModuleConfig

fun ModuleConfig.createModule(): Module = when (this) {
	is TimeModuleConfig -> TimeModule()
	is TransitionModuleConfig -> TransitionModule(this)
	is UwzConfig -> UwzModule(this)
	is FeuerwehrOoeConfig -> FeuerwehrOoeModule(this)
}
