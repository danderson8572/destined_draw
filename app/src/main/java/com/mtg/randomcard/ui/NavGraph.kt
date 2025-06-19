package com.mtg.randomcard.ui

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mtg.randomcard.viewmodel.RandomCardViewModel

object Route { const val Home = "home"; const val History = "history"; const val Detail = "detail"; const val Settings = "settings" }

@Composable
fun AppNavGraph(sharedVm: RandomCardViewModel) {
    val nav = rememberNavController()
    Crossfade(nav.currentBackStackEntry?.destination?.route) {
        NavHost(nav, startDestination = Route.Home) {
            composable(Route.Home) {
                RandomCardScreen(vm = sharedVm, onHistory = { nav.navigate(Route.History) }, onSettings = { nav.navigate(Route.Settings) })
            }
            composable(Route.History) {
                HistoryScreen(vm = sharedVm, onBack = { nav.popBackStack() }, onOpen = { nav.navigate("${Route.Detail}/$it") })
            }
            composable("${Route.Detail}/{index}", arguments = listOf(navArgument("index") { type = NavType.IntType })) { back ->
                sharedVm.cardAt(back.arguments?.getInt("index") ?: 0)?.let { DetailScreen(it) }
            }
            composable(Route.Settings) { SettingsScreen(vm = sharedVm, onBack = { nav.popBackStack() }) }
        }
    }
}