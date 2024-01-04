package com.github.simulatan.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.github.simulatan.ui.pages.Page
import com.github.simulatan.utils.AppPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(
	settings: AppPreferences,
	currentScreen: Page,
	navController: NavController,
	modifier: Modifier = Modifier
) = TopAppBar(
	title = { Text(currentScreen.title) },
	colors = TopAppBarDefaults.mediumTopAppBarColors(
		containerColor = MaterialTheme.colorScheme.primaryContainer
	),
	modifier = modifier,
	navigationIcon = { currentScreen.navigationIcon(navController) },
	actions = { currentScreen.actions(this, navController, settings) }
)
