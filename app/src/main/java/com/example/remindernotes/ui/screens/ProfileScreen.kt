package com.example.remindernotes.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.InputStream
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.remindernotes.R
import com.example.remindernotes.ui.theme.ReminderNotesTheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.example.remindernotes.viewmodel.UserViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(userViewModel: UserViewModel, navController: NavController, isDarkTheme: MutableState<Boolean>) {
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val pickImageContract = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri.value = uri
    }

    val image: Painter = imageUri.value?.let { uri ->
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        BitmapPainter(bitmap.asImageBitmap())
    } ?: painterResource(id = R.drawable.ic_launcher_background)
    val user by userViewModel.loggedInUser.collectAsState()

    val isDarkThemeOn = isDarkTheme.value

    ReminderNotesTheme(darkTheme = isDarkTheme.value) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { },
                            actions = {
                                IconButton(onClick = { isDarkTheme.value = !isDarkTheme.value }) {
                                    val icon = if (isDarkTheme.value) R.drawable.nightmode else R.drawable.nightmode1
                                    Image(
                                        painter = painterResource(id = icon),
                                        contentDescription = if (isDarkTheme.value) "Switch to light mode" else "Switch to dark mode",
                                        modifier = Modifier.size(25.dp),
                                        colorFilter = ColorFilter.tint(Color.White)
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = if(!isDarkThemeOn) colorResource(id = R.color.defaultBlue) else colorResource(id = R.color.lightBlack),
                                titleContentColor = Color.White
                            ),
                            modifier = Modifier
                                .shadow(6.dp)
                        )
                    },
                    content = {
                        if(!userViewModel.isLoggedIn()){
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background( if(!isDarkThemeOn) colorResource(id = R.color.backgroundColor) else colorResource(id = R.color.customBlack)),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp), // Set a fixed height for the card
                                    elevation = CardDefaults.cardElevation(6.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if(!isDarkThemeOn) Color.White else colorResource(id = R.color.lightBlack)
                                    )
                                ){
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ){
                                        Text(
                                            text = "You are not logged in",
                                            fontSize = 24.sp,
                                            color = if(!isDarkThemeOn) Color.Black else Color.White)
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            text = "Login or create an account to view your profile",
                                            fontWeight = FontWeight.Light,
                                            textAlign = TextAlign.Center,
                                            color = if(!isDarkThemeOn) Color.Black else Color.White
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))
                                        Divider(
                                            thickness = 2.dp,
                                            modifier = Modifier.width(250.dp),
                                            color = if(!isDarkThemeOn) colorResource(id = R.color.defaultBlue) else Color.White
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))
                                        Row() {
                                            Button(
                                                onClick = { navController.navigate("register") },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (!isDarkThemeOn) colorResource(id = R.color.defaultBlue) else Color.DarkGray,
                                                    contentColor = Color.White
                                                )
                                            ) {
                                                Text("Register")
                                            }
                                            Spacer(modifier = Modifier.width(24.dp))
                                            Button(
                                                onClick = { navController.navigate("login") },
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (!isDarkThemeOn) colorResource(id = R.color.defaultBlue) else Color.DarkGray,
                                                    contentColor = Color.White
                                                )
                                            ) {
                                                Text("Login")
                                            }
                                        }
                                    }
                                }
                            }
                        } else{
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 20.dp)
                                    .background(
                                        color =
                                        if (!isDarkThemeOn) colorResource(id = R.color.backgroundColor)
                                        else colorResource(id = R.color.customBlack)),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = image,
                                    contentDescription = "Profile Picture",
                                    modifier = Modifier
                                        .size(200.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray)
                                        .clickable { pickImageContract.launch("image/*") }, // Launch the gallery when the image is clicked
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.height(25.dp))

                                Text(
                                    text = user?.name ?:"",
                                    fontSize = 30.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if(!isDarkThemeOn) Color.Black else Color.White
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Spacer(modifier = Modifier.height(25.dp))

                                Spacer(modifier = Modifier.height(25.dp))

                                Button(
                                    onClick = { userViewModel.logout()},
                                    modifier = Modifier
                                        .width(300.dp) // Make the button take up the full width of the parent
                                        .height(60.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if(!isDarkThemeOn) colorResource(id = R.color.defaultBlue) else colorResource(id = R.color.lightBlack),
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Log Out", fontSize = 21.sp)
                                }
                            }
                        }

                    },
                    bottomBar = {
                        BottomNavigationBar(navController, isDarkTheme)
                    }
                )
    }
}

@Composable
fun ContactInfoItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth() // Ensure the row takes the full width of the parent
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}