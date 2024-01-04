package com.github.simulatan.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography().run {
	copy(
		bodyLarge = TextStyle(
			fontFamily = FontFamily.Default,
			fontWeight = FontWeight.Normal,
			fontSize = 16.sp,
			lineHeight = 24.sp,
			letterSpacing = 0.5.sp
		),
		headlineMedium = TextStyle(
			fontFamily = FontFamily.Default,
			fontWeight = FontWeight.Bold,
			fontSize = 35.sp,
			lineHeight = 35.sp,
			letterSpacing = 0.5.sp
		),
		displayMedium = TextStyle(
			fontFamily = FontFamily.Default,
			fontWeight = FontWeight.Bold,
			fontSize = 45.sp,
			lineHeight = 45.sp,
			letterSpacing = 0.5.sp
		),
		/* Other default text styles to override
	titleLarge = TextStyle(
		fontFamily = FontFamily.Default,
		fontWeight = FontWeight.Normal,
		fontSize = 22.sp,
		lineHeight = 28.sp,
		letterSpacing = 0.sp
	),
	labelSmall = TextStyle(
		fontFamily = FontFamily.Default,
		fontWeight = FontWeight.Medium,
		fontSize = 11.sp,
		lineHeight = 16.sp,
		letterSpacing = 0.5.sp
	)
	*/
	)

}
