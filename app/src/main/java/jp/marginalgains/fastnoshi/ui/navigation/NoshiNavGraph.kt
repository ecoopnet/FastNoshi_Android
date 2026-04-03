package jp.marginalgains.fastnoshi.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import jp.marginalgains.fastnoshi.ui.guidedflow.GuidedFlowScreen
import jp.marginalgains.fastnoshi.ui.home.HomeScreen

@Composable
fun NoshiNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = NoshiRoute.Home.route,
        modifier = modifier
    ) {
        composable(NoshiRoute.Home.route) {
            HomeScreen(
                onGuidedFlowClick = { navController.navigate(NoshiRoute.GuidedFlow.route) },
                onExpertClick = { navController.navigate(NoshiRoute.Expert.route) },
                onMannersGuideClick = { navController.navigate(NoshiRoute.MannersGuide.route) },
                onHistoryClick = { navController.navigate(NoshiRoute.History.route) },
                onSettingsClick = { navController.navigate(NoshiRoute.Settings.route) }
            )
        }
        composable(NoshiRoute.GuidedFlow.route) {
            GuidedFlowScreen(
                onNavigateToTextInput = { templateId, omoteGaki ->
                    navController.navigate(
                        NoshiRoute.TextInput.createRoute(templateId, omoteGaki)
                    )
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(NoshiRoute.Expert.route) {
            PlaceholderScreen("エキスパート")
        }
        composable(
            route = NoshiRoute.ExpertOmoteGaki.route,
            arguments = listOf(
                navArgument("templateId") { type = NavType.StringType }
            )
        ) {
            PlaceholderScreen("エキスパート表書き")
        }
        composable(
            route = NoshiRoute.TextInput.route,
            arguments = listOf(
                navArgument("templateId") { type = NavType.StringType },
                navArgument("omoteGaki") { type = NavType.StringType }
            )
        ) {
            PlaceholderScreen("テキスト入力")
        }
        composable(NoshiRoute.Preview.route) {
            PlaceholderScreen("プレビュー")
        }
        composable(NoshiRoute.Print.route) {
            PlaceholderScreen("印刷")
        }
        composable(NoshiRoute.Result.route) {
            PlaceholderScreen("結果")
        }
        composable(NoshiRoute.MannersGuide.route) {
            PlaceholderScreen("マナーガイド")
        }
        composable(NoshiRoute.History.route) {
            PlaceholderScreen("履歴")
        }
        composable(
            route = NoshiRoute.HistoryDetail.route,
            arguments = listOf(
                navArgument("paperId") { type = NavType.StringType }
            )
        ) {
            PlaceholderScreen("履歴詳細")
        }
        composable(NoshiRoute.Settings.route) {
            PlaceholderScreen("設定")
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = name)
    }
}
