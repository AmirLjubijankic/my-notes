package com.example.remindernotes.ui.screens


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.remindernotes.data.Task
import com.example.remindernotes.ui.Screen
import com.example.remindernotes.utils.toCustomString
import com.example.remindernotes.viewmodel.TaskViewModel
import java.time.LocalDate
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.example.remindernotes.ui.theme.ReminderNotesTheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import com.example.remindernotes.viewmodel.UserViewModel
import java.time.YearMonth
import com.example.remindernotes.R

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun TaskListScreen(navController: NavController, taskViewModel: TaskViewModel, userViewModel: UserViewModel, isDarkTheme: MutableState<Boolean>) {
    var currentYearMonth by remember { mutableStateOf(YearMonth.now()) }

    val user by userViewModel.loggedInUser.collectAsState()
    var tasksForCurrentMonth by remember { mutableStateOf(emptyList<Task>()) }


    val isDarkThemeOn = isDarkTheme.value


    LaunchedEffect(user, currentYearMonth) {
        user?.let {
            tasksForCurrentMonth = taskViewModel.sortTasks(taskViewModel.getTasksForUserByMonth(it.id, currentYearMonth))
        }
    }




    ReminderNotesTheme(darkTheme = isDarkTheme.value){
        if(user!=null){
            Scaffold(
                topBar = {
                    Column (modifier = Modifier
                        .background( if(!isDarkThemeOn) colorResource(id = R.color.defaultBlue) else colorResource(id = R.color.lightBlack))){
                        TopAppBar(
                            modifier = Modifier.padding(top = 12.dp),
                            title = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    val previousMonth = currentYearMonth.minusMonths(1)
                                    val nextMonth = currentYearMonth.plusMonths(1)

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        IconButton(onClick = {
                                            currentYearMonth = previousMonth
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowBack,
                                                contentDescription = "Previous Month"
                                            )
                                        }
                                        Text(
                                            text = previousMonth.format(
                                                DateTimeFormatter.ofPattern(
                                                    "MMM"
                                                )
                                            ), style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    Text(
                                        text = currentYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                                    )

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        IconButton(onClick = {
                                            currentYearMonth = nextMonth
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.ArrowForward,
                                                contentDescription = "Next Month"
                                            )
                                        }
                                        Text(
                                            text = nextMonth.format(DateTimeFormatter.ofPattern("MMM")),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = if(!isDarkThemeOn)colorResource(id = R.color.defaultBlue) else colorResource(id = R.color.lightBlack),
                                titleContentColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            navController.navigate("task_detail")
                        },
                        containerColor = colorResource(id = R.color.defaultBlue)) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task", tint = Color.White)
                    }
                },
                bottomBar = { BottomNavigationBar(navController, isDarkTheme) }
            ) { innerPadding ->
                LazyColumn(
                    //columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .padding(innerPadding)
                        .background(if(!isDarkThemeOn)colorResource(id = R.color.backgroundColor) else colorResource(id = R.color.customBlack))
                        .fillMaxHeight(),
                ) {
                    items(tasksForCurrentMonth) { task ->
                        TaskItem(task = task, navController, taskViewModel, isDarkThemeOn)
                    }
                }
            }
        }else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Calendar",
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 16.dp))
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = if(!isDarkThemeOn) colorResource(id = R.color.defaultBlue) else colorResource(id = R.color.lightBlack),
                            titleContentColor = Color.White
                        ),
                        modifier = Modifier.shadow(6.dp)
                    )
                },
                content={
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
                                    text = "Login or create an account to view your tasks",
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
                },
                bottomBar = { BottomNavigationBar(navController, isDarkTheme) }
            )
        }
    }
}

@Composable
fun SmallTaskItem(task: Task, navController: NavController, modifier: Modifier = Modifier, isDarkThemeOn: Boolean) {
    Card(
        modifier = modifier
            .padding(vertical = 8.dp)
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .clickable { navController.navigate("task_list") },
        colors = CardDefaults.cardColors(
            containerColor = if(!isDarkThemeOn) Color.White else colorResource(id = R.color.lightBlack)
        ),
        elevation = CardDefaults.cardElevation(6.dp) // Add this line
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = task.title,
                fontSize = 24.sp,
                color = if(!isDarkThemeOn) Color.Black else Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Due Date: ${task.dueDate.toCustomString()}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = if(!isDarkThemeOn) Color.Black else Color.White)
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = "Due Time: ${task.dueTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = if(!isDarkThemeOn) Color.Black else Color.White)
        }
    }
}

@Composable
fun TaskItem(task: Task, navController: NavController, taskViewModel: TaskViewModel, isDarkThemeOn: Boolean) {
    var showMenu by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(horizontal = 8.dp)
            .clickable { showMenu = true },
        colors = CardDefaults.cardColors(
            containerColor = if(!isDarkThemeOn) Color.White else colorResource(id = R.color.lightBlack)
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = task.title,
                fontSize = 28.sp,
                color = if(!isDarkThemeOn) Color.Black else Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = task.description,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                color = if(!isDarkThemeOn) Color.Black else Color.White)
            Spacer(modifier = Modifier.height(32.dp))
            Row (verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = task.dueDate.toCustomString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    color = if(!isDarkThemeOn) Color.Black else Color.White)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = task.dueTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    color = if(!isDarkThemeOn) Color.Black else Color.White)
            }
        }
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(text = {
                Text("Edit")
            },onClick = {
                showMenu = false
                navController.navigate("task_edit/${task.id}")
            })
            DropdownMenuItem(text = {
                Text("Delete")
            },onClick = {
                showMenu = false
                taskViewModel.deleteTask(task)
            })
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, isDarkTheme: MutableState<Boolean>) {
    val items = listOf(
        BottomNavItem.Calendar,
        BottomNavItem.Home,
        BottomNavItem.Profile
    )

    BottomAppBar (
        containerColor = if(!isDarkTheme.value) colorResource(id = R.color.defaultBlue) else colorResource(id = R.color.lightBlack),
        modifier = Modifier.shadow(6.dp)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEachIndexed { index, item ->
            if (index == 1) {
                Spacer(Modifier.weight(1f, true))
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){
                IconButton(
                    onClick = {
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier.padding(horizontal = 35.dp) // Add padding here
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(29.dp),
                        tint = if (currentRoute == item.route) Color.White else Color.White.copy(alpha = 0.7f)
                    )
                }
                Text(
                    text = item.title,
                    color = if (currentRoute == item.route) Color.White else Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            if (index == 1) {
                Spacer(Modifier.weight(1f, true))
            }
        }
    }
}

sealed class BottomNavItem(val title: String, val icon: ImageVector, val route: String) {
    object Home : BottomNavItem("Home", Icons.Filled.Home, Screen.Home.route)
    object Calendar : BottomNavItem("Calendar", Icons.Filled.DateRange, Screen.TaskList.route)
    object Profile : BottomNavItem("Profile", Icons.Filled.Person, Screen.Profile.route)
}