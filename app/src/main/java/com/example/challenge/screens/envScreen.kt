package com.example.fiap19_03.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.challenge.screens.AuthState
import com.example.challenge.screens.AuthViewModel
import com.example.challenge.screens.ThemeViewModel

@Composable
fun envScreen(navController: NavController, themeViewModel: ThemeViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val scope = rememberCoroutineScope()
    val authViewModel: AuthViewModel = viewModel()
    val colors = if (isDarkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = colors) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    DrawerContent(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { themeViewModel.toggleTheme() },
                        navController = navController
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopBar(
                        isDarkTheme = isDarkTheme,
                        onThemeChange = { themeViewModel.toggleTheme() },
                        OnOpenDrawer = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },
                        navController = navController
                    )
                }
            ) { padding ->
                ScreenEnvContent(
                    modifier = Modifier.padding(padding),
                    navController = navController,
                    isDarkTheme = isDarkTheme,
                    authViewModel = authViewModel
                )
            }
        }
    }
}


@Composable
fun ScreenEnvContent(
    navController: NavController,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    authViewModel: AuthViewModel
) {
    var showCalendar by remember { mutableStateOf(false) }
    var showInputDialog by remember { mutableStateOf(false) }
    var selectedButton by remember { mutableStateOf(-1) }


    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .width(370.dp)
                    .height(680.dp)
                    .padding(top = 25.dp),
                shape = RoundedCornerShape(30.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onPrimary),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 7.dp)
                            ) {
                                Button(
                                    onClick = {
                                        showInputDialog = !showInputDialog
                                    },
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Escrever email",
                                        tint = MaterialTheme.colorScheme.onSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Button(
                                    onClick = { showCalendar = !showCalendar },
                                    modifier = Modifier
                                        .width(80.dp)
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Data",
                                        tint = MaterialTheme.colorScheme.onSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.onPrimary),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    for (i in 1..6) {
                        SelectableButton(
                            index = i,
                            isSelected = selectedButton == i,
                            onClick = { selectedButton = if (selectedButton == i) -1 else i
                            },
                            isDarkTheme = isDarkTheme,
                            onThemeChange = { !isDarkTheme }
                        )
                    }
                }
            }
        }
    }

    if (showCalendar) {
        Dialog(onDismissRequest = { showCalendar = false }) {
            CalendarViewComposable(isDarkTheme = isDarkTheme,
                onThemeChange = { })
        }
    }
    if (showInputDialog) {
        InputDialog(onDismiss = { showInputDialog = false },
            isDarkTheme = isDarkTheme,
            onThemeChange = { })
    }

}