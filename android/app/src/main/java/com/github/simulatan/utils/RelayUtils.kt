package com.github.simulatan.utils

import com.github.simulatan.matrixcontrol.relay_provider.api.PickedRelay

fun PickedRelay.getProvider() = RelayRegistry.getRelayProvider(type)
fun PickedRelay.get() = getProvider()?.constructRelayUnsafe(settings)
