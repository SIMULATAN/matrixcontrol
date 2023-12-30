package com.github.simulatan.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.github.simulatan.matrixcontrol.protocol.message.parts.TransitionMessagePart

@Composable
fun TransitionSelectPage() =
	LazyHorizontalGrid(
		rows = GridCells.Adaptive(minSize = 150.dp),
		contentPadding = PaddingValues(20.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp),
		horizontalArrangement = Arrangement.spacedBy(16.dp)
	) {
		items(TransitionMessagePart.entries) {
			Card(
				modifier = Modifier
					.padding(4.dp)
			) {
				Text(
					text = it.fancyName,
					modifier = Modifier
						.padding(16.dp)
						.width(320.dp)
						.fillMaxHeight(),
					fontSize = 10.em,
					lineHeight = 1.2.em
				)
			}
		}
	}


@Preview(widthDp = 1920, heightDp = 1080)
@Preview(showSystemUi = false,
	device = "spec:width=2520px,height=1080px,orientation=landscape"
)
@Composable
fun TransitionSelectPagePreview() {
	TransitionSelectPage()
}
