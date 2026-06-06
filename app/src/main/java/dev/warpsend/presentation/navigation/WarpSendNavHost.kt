package dev.warpsend.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.warpsend.presentation.home.HomeRoute

object WarpSendRoutes {
    const val Home = "home"
    const val History = "history"
    const val Settings = "settings"
}

@Composable
fun WarpSendNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = WarpSendRoutes.Home
    ) {
        composable(WarpSendRoutes.Home) {
            HomeRoute()
        }
        composable(WarpSendRoutes.History) {
            // TODO: History screen
        }
        composable(WarpSendRoutes.Settings) {
            // TODO: Settings screen
        }
    }
}
