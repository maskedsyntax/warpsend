package dev.warpsend.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.warpsend.presentation.home.HomeRoute
import dev.warpsend.presentation.pairing.ReceiveRoute
import dev.warpsend.presentation.pairing.ScanRoute
import dev.warpsend.presentation.transfer.TransferDetailRoute
import androidx.navigation.NavType
import androidx.navigation.navArgument

object WarpSendRoutes {
    const val Home = "home"
    const val History = "history"
    const val Settings = "settings"
    const val Receive = "receive"
    const val Scan = "scan"
    const val TransferDetail = "transfer_detail/{sessionId}"
}

@Composable
fun WarpSendNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = WarpSendRoutes.Home
    ) {
        composable(WarpSendRoutes.Home) {
            HomeRoute(
                onSendClick = { uris -> 
                    // Store URIs in a Shared ViewModel or handle them here
                    navController.navigate(WarpSendRoutes.Scan) 
                },
                onReceiveClick = { navController.navigate(WarpSendRoutes.Receive) }
            )
        }
        composable(WarpSendRoutes.Receive) {
            ReceiveRoute(onBackClick = { navController.popBackStack() })
        }
        composable(WarpSendRoutes.Scan) {
            ScanRoute(
                onBackClick = { navController.popBackStack() },
                onPairingSuccess = { device ->
                    // For MVP, we'll navigate back to home or a transfer summary
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = WarpSendRoutes.TransferDetail,
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
            TransferDetailRoute(
                sessionId = sessionId,
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(WarpSendRoutes.History) {
            // TODO: History screen
        }
        composable(WarpSendRoutes.Settings) {
            // TODO: Settings screen
        }
    }
}
