package com.github.ntduck.int3210_3.mybookshelf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.ntduck.int3210_3.mybookshelf.ui.screens.BookScreen
import com.github.ntduck.int3210_3.mybookshelf.ui.screens.BookViewModel
import com.github.ntduck.int3210_3.mybookshelf.ui.screens.HomeScreen
import com.github.ntduck.int3210_3.mybookshelf.ui.screens.HomeViewModel
import com.github.ntduck.int3210_3.mybookshelf.ui.theme.MybookshelfTheme

enum class BookshelfScreen {
    Home,
    Detail
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MybookshelfTheme {
                BooksApp()
            }
        }
    }
}

@Composable
fun BooksApp() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()
    val bookViewModel: BookViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = BookshelfScreen.Home.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(route = BookshelfScreen.Home.name) {
            HomeScreen(
                viewModel = homeViewModel,
                onBookClick = { book ->
                    navController.navigate("${BookshelfScreen.Detail.name}/${book.id}")
                }
            )
        }
        composable(
            route = "${BookshelfScreen.Detail.name}/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            BookScreen(
                viewModel = bookViewModel,
                bookId = bookId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
