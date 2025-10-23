package com.pulz.moviedeck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pulz.moviedeck.ui.theme.MovieDeckTheme
import android.util.Log
import com.pulz.moviedeck.BuildConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Log.d("MovieDeck", "OMDb API Key: ${BuildConfig.OMDB_API_KEY}")
        Log.d("MovieDeck", "OMDb URL Base: ${BuildConfig.OMDB_BASE_URL}")
        setContent {
            MovieDeckTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "MovieDeck",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello ${BuildConfig.OMDB_API_KEY}!",
        modifier = modifier
    )
}