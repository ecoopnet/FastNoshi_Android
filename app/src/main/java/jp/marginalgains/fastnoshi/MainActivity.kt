package jp.marginalgains.fastnoshi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.marginalgains.fastnoshi.ui.navigation.NoshiNavGraph
import jp.marginalgains.fastnoshi.ui.theme.NoshiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoshiTheme {
                val navController = rememberNavController()
                NoshiNavGraph(navController = navController)
            }
        }
    }
}
