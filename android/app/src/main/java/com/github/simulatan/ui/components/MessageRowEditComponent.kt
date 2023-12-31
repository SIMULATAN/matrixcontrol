package com.github.simulatan.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.simulatan.MessageRow
import com.github.simulatan.ui.pages.Page
import com.github.simulatan.utils.navigate

@Composable
fun MessageRowEditComponent(
	navController: NavController,
	row: MessageRow,
	index: Int,
	rowEditCallback: (MessageRow) -> Unit
) =
	Row(verticalAlignment = Alignment.CenterVertically) {
		var text by remember {
			mutableStateOf(row.text)
		}

		IconButton(onClick = { navController.navigate(Page.TRANSITION_SELECT, index) }) {
			Icon(
				imageVector = Icons.Filled.ArrowDropDown,
				contentDescription = "change transition"
			)
		}
		Text(text = row.transition.fancyName, color = Color.Red)
		Spacer(modifier = Modifier.width(8.dp))
		TextField(
			value = text,
			onValueChange = { text = it; rowEditCallback(row.copy(text = it)) },
			label = { Text("Text") }
		)
	}

@Preview
@Composable
fun MessageRowEditComponentPreview() =
	MessageRowEditComponent(rememberNavController(), row = MessageRow.SAMPLE, rowEditCallback = {}, index = 0)
