package com.example.challenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.challenge.screens.AuthViewModel
import com.example.challenge.screens.ThemeViewModel
import com.example.challenge.screens.cadastroScreen
import com.example.challenge.screens.digScreen
import com.example.challenge.screens.loginScreen
import com.example.challenge.screens.perfilScreen
import com.example.challenge.ui.theme.ChallengeTheme
import com.example.fiap19_03.screens.envScreen
import com.example.fiap19_03.screens.menuScreen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            ChallengeTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val authViewModel: AuthViewModel = viewModel()
                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        composable(route = "splash") {
                            SplashScreen {
                                navController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        }
                        composable(route = "home") {
                            menuScreen(navController,themeViewModel)
                        }
                        composable(route = "login") {
                            loginScreen(navController, authViewModel)
                        }
                        composable(route = "perfil") {
                            perfilScreen(navController,themeViewModel)
                        }
                        composable(route = "cadastro") {
                            cadastroScreen(navController, authViewModel)
                        }
                        composable(route = "digite") {
                            digScreen(navController)
                        }
                        composable(route = "enviados") {
                            envScreen(navController,themeViewModel)
                        }


                    }
                }
            }
        }
    }

    @Composable
    fun SplashScreen(onTimeout: () -> Unit) {
        LaunchedEffect(Unit) {
            delay(2000)
            onTimeout()
        }

        val scale = remember { Animatable(0f) }
        LaunchedEffect(Unit) {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.colorHeader)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .width(300.dp)
                    .scale(scale.value)
                    .padding(32.dp),
                contentScale = ContentScale.Fit
            )
        }
    }

    @Composable
    fun MainScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Text("Tela Principal", color = MaterialTheme.colorScheme.onBackground)
        }
    }
}