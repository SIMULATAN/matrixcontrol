package com.github.simulatan.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.simulatan.MessageRow
import com.github.simulatan.Messages
import com.github.simulatan.matrixcontrol.protocol.message.parts.TransitionMessagePart
import com.github.simulatan.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransitionSelectPage(navigationController: NavController, messages: Messages, index: Int) =
	LazyHorizontalGrid(
		rows = GridCells.Adaptive(minSize = 140.dp),
		contentPadding = PaddingValues(20.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp),
		horizontalArrangement = Arrangement.spacedBy(16.dp)
	) {
		items(TransitionMessagePart.entries) {
			Card(
				modifier = Modifier
					.padding(4.dp),
				onClick = {
					messages[index] = messages[index].copy(transition = it)
					navigationController.navigateUp()
				}
			) {
				Column(
					modifier = Modifier.fillMaxHeight(),
					verticalArrangement = Arrangement.Center
				) {
					Text(
						text = it.fancyName,
						modifier = Modifier
							.padding(16.dp)
							.width(320.dp)
							.wrapContentHeight(align = Alignment.CenterVertically),
						style = Typography.displayMedium.copy(
							lineHeightStyle = LineHeightStyle(
								alignment = LineHeightStyle.Alignment.Bottom,
								trim = LineHeightStyle.Trim.None
							)
						),
						textAlign = TextAlign.Center
					)
				}
			}
		}
	}


@Preview(widthDp = 1920, heightDp = 816)
@Preview(showSystemUi = false,
	device = "spec:width=2520px,height=816px,orientation=landscape"
)
@Composable
fun TransitionSelectPagePreview() {
	TransitionSelectPage(rememberNavController(), mutableListOf(MessageRow.SAMPLE), 0)
}
