package frena.id.manager.ui.navigation

sealed class Screen(val route: String) {
    object About : Screen("about")
    object Favorites : Screen("favorites")
    object Map : Screen("map")
    object Permissions : Screen("permissions")
    object Settings : Screen("settings")
    object Go-Jek : Screen("gojek")
}



