package com.example.remindernotes.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.remindernotes.data.User
import com.example.remindernotes.viewmodel.UserViewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewModelScope
import com.example.remindernotes.R
import com.example.remindernotes.ui.theme.ReminderNotesTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    viewModel: UserViewModel, navController: NavController, isDarkTheme: MutableState<Boolean>
) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var password2Visibility by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var successfullRegistration by remember { mutableStateOf(false) }
    val isDarkThemeOn = isDarkTheme.value

    ReminderNotesTheme(darkTheme = isDarkTheme.value){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if(!isDarkThemeOn) colorResource(id = R.color.defaultBlue) else colorResource(id = R.color.lightBlack),
                        titleContentColor = Color.White
                    ),
                    modifier = Modifier.shadow(6.dp)
                )
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if(!isDarkThemeOn) colorResource(id = R.color.backgroundColor) else colorResource(id = R.color.customBlack)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(),
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if(!isDarkThemeOn) Color.White else colorResource(id = R.color.lightBlack))
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Register", fontSize = 24.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Name", fontWeight = FontWeight.Light) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedLabelColor = colorResource(R.color.defaultBlue),
                                    focusedBorderColor = colorResource(R.color.defaultBlue)
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = surname,
                                onValueChange = { surname = it },
                                label = { Text("Surname", fontWeight = FontWeight.Light) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedLabelColor = colorResource(R.color.defaultBlue),
                                    focusedBorderColor = colorResource(R.color.defaultBlue)
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email", fontWeight = FontWeight.Light) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedLabelColor = colorResource(R.color.defaultBlue),
                                    focusedBorderColor = colorResource(R.color.defaultBlue)
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Password", fontWeight = FontWeight.Light) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedLabelColor = colorResource(R.color.defaultBlue),
                                    focusedBorderColor = colorResource(R.color.defaultBlue)
                                ),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                        Icon(
                                            painter = if (passwordVisibility) {
                                                painterResource(id = R.drawable.ic_password_show)
                                            } else {
                                                painterResource(id = R.drawable.ic_password_hide)
                                            },
                                            contentDescription = "Toggle password visibility",
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisibility) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = password2,
                                onValueChange = { password2 = it },
                                label = { Text("Confirm password", fontWeight = FontWeight.Light) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedLabelColor = colorResource(R.color.defaultBlue),
                                    focusedBorderColor = colorResource(R.color.defaultBlue)
                                ),
                                trailingIcon = {
                                    IconButton(onClick = { password2Visibility = !password2Visibility }) {
                                        Icon(
                                            painter = if (password2Visibility) {
                                                painterResource(id = R.drawable.ic_password_show)
                                            } else {
                                                painterResource(id = R.drawable.ic_password_hide)
                                            },
                                            contentDescription = "Toggle password visibility",
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                },
                                visualTransformation = if (password2Visibility) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    if (name.isBlank() || surname.isBlank() || email.isBlank() || password.isBlank() || password2.isBlank()) {
                                        dialogMessage = "All fields must be filled"
                                        showDialog = true
                                    } else if (password.length < 8) {
                                        dialogMessage =
                                            "Password must be at least 8 characters long"
                                        showDialog = true
                                    } else if (password != password2) {
                                        dialogMessage = "Passwords do not match"
                                        showDialog = true
                                    } else {
                                        viewModel.viewModelScope.launch {
                                            viewModel.register(
                                                User(
                                                    name = name,
                                                    surname = surname,
                                                    email = email,
                                                    password = password
                                                )
                                            )
                                        }
                                        dialogMessage = "Registration successful"
                                        successfullRegistration = true
                                        showDialog = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if(!isDarkThemeOn)colorResource(id = R.color.defaultBlue) else Color.DarkGray,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("REGISTER")
                            }
                        }
                    }
                }
            })
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = {
            showDialog = false
            if(successfullRegistration){
                navController.navigate("home")
            }
        }, title = {
            Text(text = "Alert")
        }, text = {
            Text(dialogMessage)
        }, confirmButton = {
            Button(onClick = {
                showDialog = false
                if(successfullRegistration){
                    navController.navigate("home")
                }
            }) {
                Text("OK")
            }
        })
    }
}