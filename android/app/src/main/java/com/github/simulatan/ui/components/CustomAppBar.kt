package com.github.simulatan.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.github.simulatan.ui.pages.Page

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(
	currentScreen: Page,
	navController: NavController,
	modifier: Modifier = Modifier
) = TopAppBar(
	title = { Text(currentScreen.title) },
	colors = TopAppBarDefaults.mediumTopAppBarColors(
		containerColor = MaterialTheme.colorScheme.primaryContainer
	),
	modifier = modifier,
	navigationIcon = {
		if (navController.previousBackStackEntry != null) {
			IconButton(onClick = { navController.navigateUp() }) {
				Icon(
					imageVector = Icons.Filled.ArrowBack,
					contentDescription = "back"
				)
			}
		}
		currentScreen.navigationIcon(navController)
	}
)
