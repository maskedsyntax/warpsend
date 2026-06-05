package dev.warpsend.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.warpsend.presentation.home.HomeRoute

object WarpSendRoutes {
    const val Home = "home"
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
    }
}
