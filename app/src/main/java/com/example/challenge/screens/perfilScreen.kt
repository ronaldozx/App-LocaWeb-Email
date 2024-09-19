package com.example.challenge.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.challenge.R
import com.example.fiap19_03.screens.InputDialog
import kotlinx.coroutines.launch

@Composable
fun perfilScreen(navController: NavController, themeViewModel: ThemeViewModel) {
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    MaterialTheme(colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()) {
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
                ScreenPerfilContent(
                    modifier = Modifier.padding(padding),
                    navController = navController,
                    isDarkTheme = isDarkTheme,
                    authViewModel = viewModel(),
                    onThemeChange = { themeViewModel.toggleTheme() },
                    userViewModel = viewModel()
                )
            }
        }
    }
}


@Composable
fun DrawerContent(
    navController: NavController,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isDarkTheme) MaterialTheme.colorScheme.background else Color.White
    val textoColor = if (isDarkTheme) MaterialTheme.colorScheme.onBackground else Color.Black

    Column(modifier = modifier
        .background(backgroundColor)
        .fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.perfil),
            contentDescription = "Perfil",
            modifier = Modifier
                .size(150.dp)
                .padding(top = 20.dp, start = 20.dp, bottom = 10.dp)
        )

        Text(
            text = "LocaWeb Emails",
            fontSize = 24.sp,
            color = textoColor,
            modifier = Modifier.padding(16.dp)
        )
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "Inbox",
                    tint = textoColor
                )
            },
            label = {
                Text(
                    text = "Inbox",
                    fontSize = 17.sp,
                    color = textoColor,
                    modifier = Modifier.padding(16.dp)
                )
            },
            selected = false,
            onClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(8.dp))

        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Enviados",
                    tint = textoColor
                )
            },
            label = {
                Text(
                    text = "Enviados",
                    fontSize = 17.sp,
                    color = textoColor,
                    modifier = Modifier.padding(16.dp)
                )
            },
            selected = false,
            onClick = {
                navController.navigate("enviados")
            }
        )
        Spacer(modifier = Modifier.height(8.dp))

        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Spam",
                    tint = textoColor
                )
            },
            label = {
                Text(
                    text = "Spam",
                    fontSize = 17.sp,
                    color = textoColor,
                    modifier = Modifier.padding(16.dp)
                )
            },
            selected = false,
            onClick = { /*TODO*/ }
        )
        Spacer(modifier = Modifier.height(8.dp))

        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Filled.WbSunny else Icons.Default.NightsStay,
                    contentDescription = if (isDarkTheme) "Modo Claro" else "Modo Escuro",
                    tint = textoColor,
                )
            },
            label = {
                Text(
                    text = if (isDarkTheme) "Modo Claro" else "Modo Escuro",
                    fontSize = 17.sp,
                    color = textoColor,
                    modifier = Modifier.padding(16.dp)
                )
            },
            selected = false,
            onClick = { onThemeChange() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenPerfilContent(
    navController: NavController,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
    onThemeChange: () -> Unit
) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    var showInputDialog by remember { mutableStateOf(false) }

    val userData by userViewModel.userData.observeAsState()
    val textoColor = if (isDarkTheme) Color.Black else Color.Black

    LaunchedEffect(Unit) {
        userViewModel.fetchUserData()
    }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Card(
            modifier = Modifier
                .width(360.dp)
                .height(500.dp)
                .align(Alignment.Center),
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.colorCard)),
            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
            shape = RoundedCornerShape(30.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 10.dp, start = 40.dp, end = 40.dp, bottom = 40.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Email",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    color = textoColor,
                    modifier = Modifier.padding(top = 10.dp, bottom = 1.dp)
                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = userData?.email ?: "",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .padding(top = 15.dp)
                            .border(
                                width = 1.dp,
                                color = textoColor,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(color = textoColor)
                    )
                }

                Text(
                    text = "Nome",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(top = 10.dp, bottom = 1.dp),
                    color = textoColor
                    )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = userData?.nome ?: "",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .padding(top = 15.dp)
                            .border(
                                width = 1.dp,
                                color = textoColor,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(color = textoColor)
                    )
                }

                Text(
                    text = "Telefone",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(top = 10.dp, bottom = 1.dp),
                    color = textoColor
                    )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = userData?.telefone ?: "",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .padding(top = 15.dp)
                            .border(
                                width = 1.dp,
                                color = textoColor,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(color = textoColor)
                    )
                }

                Text(
                    text = "Data de Nascimento",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(top = 10.dp, bottom = 1.dp),
                    color = textoColor

                    )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = userData?.dtNascimento ?: "",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .padding(top = 15.dp)
                            .border(
                                width = 1.dp,
                                color = textoColor,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = TextStyle(color = textoColor)
                    )
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxSize()
                .padding(bottom = 150.dp)
        ){
            Button(
                onClick = { navController.navigate("home") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),

                ) {
                Icon(
                    imageVector = Icons.Default.Reply,
                    tint = MaterialTheme.colorScheme.onSecondary,
                    contentDescription = "Voltar"
                )
            }
        }
    }
    if (showInputDialog) {
        InputDialog(
            onDismiss = { showInputDialog = false },
            isDarkTheme = isDarkTheme,
            onThemeChange = onThemeChange
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit,
    modifier: Modifier = Modifier,
    OnOpenDrawer: () -> Unit,
    navController: NavController
) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(50.dp)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = OnOpenDrawer) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate("perfil") }) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Perfil",
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    )
}
@Composable
fun lightColorScheme(): ColorScheme{
    val temaClaro1 = colorResource(id = R.color.temaClaro1)
    val temaClaro2 = colorResource(id = R.color.temaClaro2)
    val temaClaro3 = colorResource(id = R.color.temaClaro3)
    val temaClaro4 = colorResource(id = R.color.temaClaro4)


    return lightColorScheme(
        primary = temaClaro1,
        onPrimary = temaClaro2,
        secondary = temaClaro3,
        onSecondary = temaClaro4


    )
}

@Composable
fun darkColorScheme(): ColorScheme {
    val temaEscuro1 = colorResource(id = R.color.temaEscuro1)
    val temaEscuro2 = colorResource(id = R.color.temaEscuro2)
    val temaEscuro3 = colorResource(id = R.color.temaEscuro3)
    val temaEscuro4 = colorResource(id = R.color.temaEscuro4)


    return darkColorScheme(
        primary = temaEscuro1,
        onPrimary = temaEscuro2,
        secondary = temaEscuro3,
        onSecondary= temaEscuro4,

        )
}