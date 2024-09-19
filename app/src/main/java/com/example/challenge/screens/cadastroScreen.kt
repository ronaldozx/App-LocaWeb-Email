package com.example.challenge.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.challenge.R
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun cadastroScreen(navController: NavController,authViewModel: AuthViewModel) {
    var texto by remember {
        mutableStateOf("")
    }
    var nome by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var dtNascimento by remember {
        mutableStateOf("")
    }
    var telefone by remember {
        mutableStateOf("")
    }
    var confirmeSuaSenha by remember {
        mutableStateOf("")
    }
    val authState = authViewModel.authState.observeAsState()

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(

            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(colorResource(id = R.color.colorHeader))
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo do app",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .padding(vertical = 12.dp)
                    .align(Alignment.Center)
                    .offset(y = (-70).dp)
            )
        }


        Card(
            modifier = Modifier
                .width(360.dp)
                .height(720.dp)
                .align(Alignment.BottomCenter),

            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.colorCard)),
            elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
            shape = RoundedCornerShape(30.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 40.dp, top = 10.dp, end = 40.dp, bottom = 20.dp )
                    .fillMaxWidth(),

            ) {
                Text(
                    text = "Nome",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(top = 10.dp, bottom = 1.dp)

                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = nome,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .padding(top = 15.dp)
                            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(20.dp)),
                        onValueChange = { nome = it },

                        label = {
                            Text(text = "")
                        },
                        placeholder = {
                            Text(text = "")
                        },

                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                    )
                }
                Text(
                    text = "Email",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(top = 10.dp, bottom = 1.dp)

                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = email,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(20.dp)),
                        onValueChange = { email = it },
                        label = {
                            Text(text = "")
                        },
                        placeholder = {
                            Text(text = "")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                    )
                }
                Text(
                    text = "Data de Nascimento",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(top = 10.dp, bottom = 1.dp)

                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = dtNascimento,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(20.dp)),
                        onValueChange = { dtNascimento = it },
                        label = {
                            Text(text = "")
                        },
                        placeholder = {
                            Text(text = "")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                    )
                }
                Text(
                    text = "Telefone",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(top = 10.dp, bottom = 1.dp)

                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = telefone,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(20.dp)),
                        onValueChange = { telefone = it },
                        label = {
                            Text(text = "")
                        },
                        placeholder = {
                            Text(text = "")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                    )
                }
                Text(
                    text = "Senha",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(top = 10.dp, bottom = 1.dp)

                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = password,
                        modifier = Modifier
                            .fillMaxWidth()

                            .clip(RoundedCornerShape(20.dp))
                            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(20.dp)),
                        onValueChange = { password = it },
                        label = {
                            Text(text = "")
                        },
                        placeholder = {
                            Text(text = "")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),

                        )
                }
                Text(
                    text = "Confirmar Senha",
                    style = TextStyle(
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(top = 10.dp, bottom = 1.dp)

                )
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = confirmeSuaSenha,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(20.dp)),
                        onValueChange = { confirmeSuaSenha = it },
                        label = {
                            Text(text = "")
                        },
                        placeholder = {
                            Text(text = "")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {



                    Button(
                        onClick = { authViewModel.cadastro(email, password, dtNascimento, telefone, nome)
                                    navController.navigate("login")},
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.colorHeader)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier

                            .width(200.dp)

                    ) {
                        Text(text = "Cadastrar")
                    }
                }

            }
    }
}
