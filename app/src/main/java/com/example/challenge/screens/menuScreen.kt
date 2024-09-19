package com.example.fiap19_03.screens

import android.util.Log
import android.widget.CalendarView
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavController
import com.example.challenge.R
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.challenge.screens.AuthState
import com.example.challenge.screens.AuthViewModel
import com.example.challenge.screens.ThemeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun menuScreen(navController: NavController, themeViewModel: ThemeViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    MaterialTheme(colorScheme = if (themeViewModel.isDarkTheme.collectAsState().value) darkColorScheme() else lightColorScheme()) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    DrawerContent(
                        isDarkTheme = themeViewModel.isDarkTheme.collectAsState().value,
                        onThemeChange = { themeViewModel.toggleTheme() },
                        navController = navController
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopBar(
                        isDarkTheme = themeViewModel.isDarkTheme.collectAsState().value,
                        onThemeChange = { themeViewModel.toggleTheme() },
                        OnOpenDrawer = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        },
                        navController= navController
                    )
                }
            ) { padding ->
                ScreenContent(
                    modifier = Modifier.padding(padding),
                    navController = navController,
                    isDarkTheme = themeViewModel.isDarkTheme.collectAsState().value,
                    authViewModel = viewModel(),
                    onThemeChange = { themeViewModel.toggleTheme() }
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
    val textColor = if (isDarkTheme) MaterialTheme.colorScheme.onBackground else Color.Black

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
            color = textColor,
            modifier = Modifier.padding(16.dp)
        )
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "Inbox",
                    tint = textColor
                )
            },
            label = {
                Text(
                    text = "Inbox",
                    fontSize = 17.sp,
                    color = textColor,
                    modifier = Modifier.padding(16.dp)
                )
            },
            selected = false,
            onClick = { navController.navigate("home") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Enviados",
                    tint = textColor
                )
            },
            label = {
                Text(
                    text = "Enviados",
                    fontSize = 17.sp,
                    color = textColor,
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
                    tint = textColor
                )
            },
            label = {
                Text(
                    text = "Spam",
                    fontSize = 17.sp,
                    color = textColor,
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
                    tint = textColor,
                )
            },
            label = {
                Text(
                    text = if (isDarkTheme) "Modo Claro" else "Modo Escuro",
                    fontSize = 17.sp,
                    color = textColor,
                    modifier = Modifier.padding(16.dp)
                )
            },
            selected = false,
            onClick = { onThemeChange() }
        )
    }
}


@Composable
fun ScreenContent(
    navController: NavController,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    authViewModel: AuthViewModel,
    onThemeChange: () -> Unit
) {
    var showCalendar by remember { mutableStateOf(false) }
    var showInputDialog by remember { mutableStateOf(false) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    var selectedButton by remember { mutableStateOf(-1) }

    var showReadEmail by remember { mutableStateOf(false) }
    var emailData by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    LaunchedEffect(authState.value) {
        when (authState.value) {
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
                            onClick = {
                                selectedButton = if (selectedButton == i) -1 else i
                            },
                            isDarkTheme = isDarkTheme,
                            onThemeChange = onThemeChange
                        )
                    }
                }
            }
        }
    }
    if (showCalendar) {
        Dialog(onDismissRequest = { showCalendar = false }) {
            CalendarViewComposable(
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange
            )
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
fun CalendarViewComposable(isDarkTheme: Boolean,
                           onThemeChange: () -> Unit,) {
    val textColor = if (isDarkTheme) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary

    AndroidView(
        factory = { context ->
            CalendarView(context).apply {
                setOnDateChangeListener { _, year, month, dayOfMonth ->
                    // Lógica ao selecionar uma data
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(textColor),
    )
}

@Composable
fun SelectableButton(
    index: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit
) {
    val currentIcon = if (isSelected) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
    val textColor = if (isDarkTheme) MaterialTheme.colorScheme.onBackground else Color.Black
    var showReadEmail by remember { mutableStateOf(false) }
    var emailData by remember { mutableStateOf<Map<String, String>?>(null) }

    val db = FirebaseFirestore.getInstance()

    // Fetch email data when the component is composed
    LaunchedEffect(index) {
        fetchEmailData(index, db) { data ->
            emailData = data?.mapValues { it.value as? String ?: "" } // Convert Any to String
        }
    }

    // Condicionalmente renderizar o botão apenas se houver dados de e-mail
    if (emailData != null) {
        Button(
            onClick = {
                showReadEmail = !showReadEmail
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .drawBehind {
                    drawLine(
                        color = textColor,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
            shape = RoundedCornerShape(0.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    modifier = Modifier
                        .width(35.dp)
                        .height(35.dp),
                    painter = painterResource(id = R.drawable.perfil),
                    contentDescription = "home"
                )
                Column {
                    Text(
                        text = emailData?.get("assunto") ?: "Título do Email",
                        fontSize = 20.sp,
                        color = textColor,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = emailData?.get("textoEmail")?.take(30) ?: "Texto de começo do email",
                        fontSize = 15.sp,
                        color = textColor,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                IconButton(
                    onClick = onClick,
                    modifier = Modifier.size(35.dp)
                ) {
                    Icon(
                        imageVector = currentIcon,
                        contentDescription = "Favorito",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }

    if (showReadEmail && emailData != null) {
        ReadEmailDialog(
            emailData = emailData!!,
            onDismiss = { showReadEmail = false },
            isDarkTheme = isDarkTheme,
            onThemeChange = onThemeChange
        )
    }
}

fun fetchEmailData(index: Int, db: FirebaseFirestore, onResult: (Map<String, Any>?) -> Unit) {
    db.collection("emails")
        .get()
        .addOnSuccessListener { result ->
            val document = result.documents.getOrNull(index)
            if (document != null && document.data != null) {
                onResult(document.data)
            } else {
                onResult(null) // Não há e-mails
            }
        }
        .addOnFailureListener { exception ->
            Log.e("Firestore", "Erro ao buscar documento: ", exception)
            onResult(null)
        }
}








@Composable
fun ReadEmailDialog(
    emailData: Map<String, String>,
    onDismiss: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit
) {
    val textColor = if (isDarkTheme) MaterialTheme.colorScheme.onBackground else Color.Black

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 8.dp,
            modifier = Modifier
                .width(500.dp)
                .height(550.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onPrimary)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
                Text(
                    text = emailData["assunto"] ?: "Sem Assunto",
                    fontSize = 20.sp,
                    color = textColor,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 10.dp)

                )
                Text(
                    text = emailData["textoEmail"] ?: "Sem conteúdo",
                    fontSize = 16.sp,
                    color = textColor,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(300.dp)
                        .drawBehind {
                            drawLine(
                                color = textColor,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 0.5.dp.toPx()
                            )
                        }
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp, start = 60.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Reply,
                            contentDescription = "Responder",
                            tint = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Responder",
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Reply,
                            contentDescription = "Encaminhar",
                            tint = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier
                                .size(24.dp)
                                .graphicsLayer(rotationY = 180f)

                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Encaminhar")
                    }
                }

            }
        }
    }
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDialog(
    onDismiss: () -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit
) {
    var envio by remember { mutableStateOf("") }
    var assunto by remember { mutableStateOf("") }
    var textoEmail by remember { mutableStateOf("") }
    val textColor = if (isDarkTheme) MaterialTheme.colorScheme.onBackground else Color.Black
    var response by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val remetente = currentUser?.email ?: ""

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 8.dp,
            modifier = Modifier
                .width(500.dp)
                .height(550.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onPrimary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onPrimary)
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }

                OutlinedTextField(
                    value = envio,
                    onValueChange = { envio = it },
                    label = { Text("Para:", color = textColor) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onPrimary),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    )
                )

                OutlinedTextField(
                    value = assunto,
                    onValueChange = { assunto = it },
                    label = { Text("Assunto:", color = textColor) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .drawBehind {
                            drawLine(
                                color = textColor,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 0.5.dp.toPx()
                            )
                        },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    )
                )

                OutlinedTextField(
                    value = textoEmail,
                    onValueChange = { textoEmail = it },
                    label = { Text("Escrever E-mail:", color = textColor) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .drawBehind {
                            drawLine(
                                color = textColor,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 0.5.dp.toPx()
                            )
                        },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    )
                )


                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Button(
                        onClick = {
                            val emailData = mapOf(
                                "envio" to envio,
                                "assunto" to assunto,
                                "textoEmail" to textoEmail,
                                "remetente" to remetente,
                                "destinatario" to envio
                            )

                            db.collection("emails")
                                .add(emailData)
                                .addOnSuccessListener {
                                    response = "E-mail enviado com sucesso!"
                                    envio = ""
                                    assunto = ""
                                    textoEmail = ""
                                }
                                .addOnFailureListener { e ->
                                    response = "Falha ao enviar e-mail: ${e.message}"
                                }
                            onDismiss()

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Enviar",
                            tint = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Enviar",
                            color = textColor,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(horizontal = 15.dp)
                        )
                    }
                }
                if (response.isNotEmpty()) {
                    Text(
                        text = response,
                        color = if (response.startsWith("Falha")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }
        }
    }
}



