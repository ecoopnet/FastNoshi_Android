package jp.marginalgains.fastnoshi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = NoshiBrown,
    onPrimary = NoshiOnBrown,
    primaryContainer = NoshiBrownContainer,
    secondary = NoshiRed,
    onSecondary = NoshiOnRed,
    secondaryContainer = NoshiRedContainer,
    tertiary = NoshiGold,
    onTertiary = NoshiOnGold,
    tertiaryContainer = NoshiGoldContainer,
    background = NoshiBackground,
    onBackground = NoshiOnBackground,
    surface = NoshiSurface,
    onSurface = NoshiOnSurface
)

private val DarkColorScheme = darkColorScheme(
    primary = NoshiBrownDark,
    onPrimary = NoshiOnBrownDark,
    primaryContainer = NoshiBrownContainerDark,
    secondary = NoshiRedDark,
    onSecondary = NoshiOnRedDark,
    secondaryContainer = NoshiRedContainerDark,
    tertiary = NoshiGoldDark,
    onTertiary = NoshiOnGoldDark,
    tertiaryContainer = NoshiGoldContainerDark,
    background = NoshiBackgroundDark,
    onBackground = NoshiOnBackgroundDark,
    surface = NoshiSurfaceDark,
    onSurface = NoshiOnSurfaceDark
)

@Composable
fun NoshiTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NoshiTypography,
        content = content
    )
}
