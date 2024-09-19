package com.example.challenge.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.challenge.R
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun loginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                    .offset(y = (-20).dp)
            )
        }


        Card(
            modifier = Modifier
                .width(360.dp)
                .height(250.dp)
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
                            .clip(RoundedCornerShape(20.dp))
                            .padding(top = 15.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        onValueChange = { email = it },

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
                            .padding(top = 15.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        onValueChange = { password = it },

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
            }
        }
        Column(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 100.dp, start = 40.dp, end = 40.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    authViewModel.login(email, password)
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.colorHeader)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Entrar")
            }

            Button(
                onClick = { navController.navigate("cadastro") },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.colorHeader)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Cadastrar")
            }

            Button(
                onClick = { navController.navigate("digite") },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.colorHeader)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Esqueci a senha")
            }
        }
    }
}
