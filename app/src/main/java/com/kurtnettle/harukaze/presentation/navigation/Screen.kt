package com.kurtnettle.harukaze.presentation.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector
import com.kurtnettle.harukaze.R

sealed class Screen(
    val route: String,
    @StringRes val titleRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : Screen("home", R.string.home, Icons.Rounded.Home, Icons.Outlined.Home)
    data object About :
        Screen("about", R.string.about, Icons.Rounded.Person, Icons.Rounded.PersonOutline)

    companion object {
        val screens: List<Screen> = listOf(Home, About)

        fun fromIndex(index: Int): Screen {
            if (screens.isEmpty()) return Home
            return screens.getOrNull(index.coerceIn(0, screens.lastIndex)) ?: Home
        }

        val Screen.pageIndex: Int
            get() = screens.indexOf(this).takeIf { it != -1 } ?: 0
    }
}