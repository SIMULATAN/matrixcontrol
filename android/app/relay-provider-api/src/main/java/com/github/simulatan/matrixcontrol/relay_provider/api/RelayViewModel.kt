package com.github.simulatan.matrixcontrol.relay_provider.api

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RelayViewModel(initial: PickedRelay) : ViewModel() {
	val pickedRelay = mutableStateOf(initial)

	// unchecked to avoid problems with kotlinx.serialization
	@Suppress("UNCHECKED_CAST")
	fun <S : RelaySettings> getSettings() = pickedRelay.value.settings as S


	fun updateRelay(newRelay: PickedRelay) {
		pickedRelay.value = newRelay
	}

	fun updateSettings(newSettings: RelaySettings) {
		updateRelay(pickedRelay.value.copy(settings = newSettings))
	}
}

class RelayViewModelFactory(private val initial: PickedRelay) : ViewModelProvider.NewInstanceFactory() {
	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel> create(modelClass: Class<T>): T = RelayViewModel(initial) as T
}
