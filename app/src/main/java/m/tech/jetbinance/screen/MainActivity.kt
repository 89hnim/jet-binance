package m.tech.jetbinance.screen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import m.tech.jetbinance.screen.detail.DetailScreen
import m.tech.jetbinance.screen.home.HomeScreen
import m.tech.jetbinance.screen.home.viewmodel.HomeViewModel
import m.tech.jetbinance.ui.theme.JetBinanceTheme
import m.tech.jetbinance.ui.theme.JetColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Class
                .forName("androidx.compose.material3.TabRowKt")
                .getDeclaredField("ScrollableTabRowMinimumTabWidth").apply {
                    isAccessible = true
                }.set(this, 0f)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setContent {
            JetBinanceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = JetColor.surface,
                ) {
                    MyAppNavHost()
                }
            }
        }
    }
}

@Composable
fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "home"
) {
    val lazyListState = rememberLazyListState()
    val homeViewModel = HomeViewModel()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("home") {
            HomeScreen(
                homeViewModel = homeViewModel,
                listState = lazyListState,
                onNavigateToDetail = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }
        composable(
            "detail/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            DetailScreen(id = backStackEntry.arguments?.getString("id").orEmpty(), navController)
        }
    }
}