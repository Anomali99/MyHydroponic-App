package id.my.anomali99.myhydroponic.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Settings : Screen("settings")
}