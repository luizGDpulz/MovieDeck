package com.pulz.moviedeck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.pulz.moviedeck.data.api.RetrofitClient
import com.pulz.moviedeck.data.repository.MovieRepository
import com.pulz.moviedeck.ui.screens.HomeScreen
import com.pulz.moviedeck.ui.screens.MovieDetailScreen
import com.pulz.moviedeck.ui.theme.MovieDeckTheme
import com.pulz.moviedeck.viewmodel.MovieViewModel
import com.pulz.moviedeck.viewmodel.MovieViewModelFactory
import com.pulz.moviedeck.viewmodel.MovieDetailViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieDeckTheme {
                val repository = MovieRepository(RetrofitClient.api)
                val factory = MovieViewModelFactory(repository)
                val movieViewModel: MovieViewModel = viewModel(factory = factory)

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "home") {

                    // 🏠 Tela principal
                    composable("home") {
                        HomeScreen(
                            movieViewModel = movieViewModel,
                            onMovieClick = { movie ->
                                movie.imdbID?.let { id ->
                                    navController.navigate("details/$id")
                                }
                            }
                        )
                    }

                    // 🎬 Tela de detalhes (passa apenas o IMDb ID)
                    composable(
                        route = "details/{imdbID}",
                        arguments = listOf(navArgument("imdbID") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val imdbID = backStackEntry.arguments?.getString("imdbID") ?: return@composable

                        val detailViewModel: MovieDetailViewModel = viewModel(
                            factory = MovieViewModelFactory(repository)
                        )

                        MovieDetailScreen(
                            imdbID = imdbID,
                            viewModel = detailViewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
