package frena.id.manager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import frena.id.manager.ui.about.AboutScreen
import frena.id.manager.ui.favorites.FavoritesScreen
import frena.id.manager.ui.map.MapScreen
import frena.id.manager.ui.map.MapViewModel
import frena.id.manager.ui.permissions.PermissionsScreen
import frena.id.manager.ui.settings.SettingsScreen
import frena.id.manager.ui.gojek.GojekScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
) {
    val mapViewModel: MapViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Permissions.route,
    ) {
        composable(route = Screen.About.route) {
            AboutScreen(navController = navController)
        }
        composable(route = Screen.Favorites.route) {
            FavoritesScreen(navController = navController, mapViewModel)
        }
        composable(route = Screen.Map.route) {
            MapScreen(navController = navController, mapViewModel)
        }
        composable(route = Screen.Permissions.route) {
            PermissionsScreen(navController = navController)
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }
        
        composable(route = Screen.Gojek.route) {
            GojekScreen(navController = navController)
        }
//        composable(route = Screen.Grab.route) {
//            GrabScreen(navController = navController)
//        }
//        composable(route = Screen.Shopee.route) {
//            ShopeeScreen(navController = navController)
//        }
        
    }
}
