package com.github.simulatan.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.simulatan.matrixcontrol.protocol.message.parts.TransitionMessagePart

@Composable
fun TransitionSelectComponent() =
	LazyVerticalGrid(
		columns = GridCells.Adaptive(minSize = 128.dp),
		contentPadding = PaddingValues(
			start = 12.dp,
			top = 16.dp,
			end = 12.dp,
			bottom = 16.dp
		),
		content = {
			items(TransitionMessagePart.entries) {
				Card(
					modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
				) {
					Text(text = it.fancyName, modifier = Modifier.padding(16.dp), fontSize = 10.sp)
				}
			}
		})