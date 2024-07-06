package com.example.jawlah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.jawlah.presentation.feature.NavGraphs
import com.example.jawlah.presentation.feature.destinations.AskGemiScreenDestination
import com.example.jawlah.presentation.feature.destinations.MyPlansScreenDestination
import com.example.jawlah.presentation.theme.JawlahTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val navigateTo = intent.getStringExtra("navigateTo")

        setContent {
            JawlahTheme {
                // A surface container using the 'background' color from the theme
                Scaffold { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        color = MaterialTheme.colorScheme.background,
                    ) {
                        val startRoute = if (navigateTo == "LandmarkLens") {
                            AskGemiScreenDestination
                        } else {
                            MyPlansScreenDestination
                        }

                        DestinationsNavHost(
                            navGraph = NavGraphs.app,
                            startRoute = startRoute
                        )
                    }
                }
            }
        }
    }
}