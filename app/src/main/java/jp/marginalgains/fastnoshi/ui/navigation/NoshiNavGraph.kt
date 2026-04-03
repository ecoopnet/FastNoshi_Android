package jp.marginalgains.fastnoshi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import jp.marginalgains.fastnoshi.ui.expert.ExpertOmoteGakiScreen
import jp.marginalgains.fastnoshi.ui.expert.TemplateSelectionScreen
import jp.marginalgains.fastnoshi.ui.guide.MannersGuideScreen
import jp.marginalgains.fastnoshi.ui.guidedflow.GuidedFlowScreen
import jp.marginalgains.fastnoshi.ui.history.HistoryDetailScreen
import jp.marginalgains.fastnoshi.ui.history.HistoryScreen
import jp.marginalgains.fastnoshi.ui.home.HomeScreen
import jp.marginalgains.fastnoshi.ui.input.TextInputScreen
import jp.marginalgains.fastnoshi.ui.preview.PreviewScreen
import jp.marginalgains.fastnoshi.ui.print.PrintScreen
import jp.marginalgains.fastnoshi.ui.settings.SettingsScreen

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
            TemplateSelectionScreen(
                onNavigateToOmoteGaki = { templateId ->
                    navController.navigate(
                        NoshiRoute.ExpertOmoteGaki.createRoute(templateId)
                    )
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = NoshiRoute.ExpertOmoteGaki.route,
            arguments = listOf(
                navArgument("templateId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val templateId = backStackEntry.arguments?.getString("templateId") ?: return@composable
            ExpertOmoteGakiScreen(
                templateId = templateId,
                onNavigateToTextInput = { id, omoteGaki ->
                    navController.navigate(NoshiRoute.TextInput.createRoute(id, omoteGaki))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = NoshiRoute.TextInput.route,
            arguments = listOf(
                navArgument("templateId") { type = NavType.StringType },
                navArgument("omoteGaki") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val templateId = backStackEntry.arguments?.getString("templateId") ?: return@composable
            val omoteGaki = backStackEntry.arguments?.getString("omoteGaki") ?: return@composable
            TextInputScreen(
                templateId = templateId,
                omoteGaki = omoteGaki,
                onNavigateToPreview = { tId, og, names ->
                    navController.navigate(NoshiRoute.Preview.createRoute(tId, og, names))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = NoshiRoute.Preview.route,
            arguments = listOf(
                navArgument("templateId") { type = NavType.StringType },
                navArgument("omoteGaki") { type = NavType.StringType },
                navArgument("names") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val templateId = backStackEntry.arguments?.getString("templateId") ?: return@composable
            val omoteGaki = backStackEntry.arguments?.getString("omoteGaki") ?: return@composable
            val namesStr = backStackEntry.arguments?.getString("names") ?: return@composable
            val names = namesStr.split(",").filter { it.isNotEmpty() }
            PreviewScreen(
                templateId = templateId,
                omoteGaki = omoteGaki,
                names = names,
                onNavigateToPrint = { nav ->
                    navController.navigate(
                        NoshiRoute.Print.createRoute(
                            nav.templateId,
                            nav.omoteGaki,
                            nav.names,
                            nav.fontSetId,
                            nav.omoteGakiFontSize,
                            nav.nameFontSize,
                            nav.paperSize
                        )
                    )
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = NoshiRoute.Print.route,
            arguments = listOf(
                navArgument("templateId") { type = NavType.StringType },
                navArgument("omoteGaki") { type = NavType.StringType },
                navArgument("names") { type = NavType.StringType },
                navArgument("fontSetId") { type = NavType.StringType },
                navArgument("omoteGakiFontSize") { type = NavType.StringType },
                navArgument("nameFontSize") { type = NavType.StringType },
                navArgument("paperSize") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments ?: return@composable
            val templateId = args.getString("templateId") ?: return@composable
            val omoteGaki = args.getString("omoteGaki") ?: return@composable
            val namesStr = args.getString("names") ?: return@composable
            val names = namesStr.split(",").filter { it.isNotEmpty() }
            val fontSetId = args.getString("fontSetId") ?: "mincho"
            val omoteGakiFontSize = args.getString("omoteGakiFontSize")?.toFloatOrNull() ?: 28f
            val nameFontSize = args.getString("nameFontSize")?.toFloatOrNull() ?: 24f
            val paperSize = args.getString("paperSize") ?: "A4"
            PrintScreen(
                templateId = templateId,
                omoteGaki = omoteGaki,
                names = names,
                fontSetId = fontSetId,
                omoteGakiFontSize = omoteGakiFontSize,
                nameFontSize = nameFontSize,
                paperSize = paperSize,
                onNavigateHome = {
                    navController.popBackStack(NoshiRoute.Home.route, inclusive = false)
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(NoshiRoute.MannersGuide.route) {
            MannersGuideScreen(onBackClick = { navController.popBackStack() })
        }
        composable(NoshiRoute.History.route) {
            HistoryScreen(
                onPaperClick = { paperId ->
                    navController.navigate(NoshiRoute.HistoryDetail.createRoute(paperId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(
            route = NoshiRoute.HistoryDetail.route,
            arguments = listOf(
                navArgument("paperId") { type = NavType.StringType }
            )
        ) {
            HistoryDetailScreen(onBackClick = { navController.popBackStack() })
        }
        composable(NoshiRoute.Settings.route) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}
