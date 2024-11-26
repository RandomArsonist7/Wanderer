package com.example.wanderer

import Steps
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LinearScale
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.CompassCalibration
import androidx.compose.material.icons.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LinearScale
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app1jetpackcompose.Home
import com.example.wanderer.ui.theme.WandererTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            WandererTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    MyBottomAppBar()
                }
            }
        }
    }
}

@Composable
fun MyBottomAppBar(){
    val navigationController = rememberNavController()
    val context = LocalContext.current.applicationContext
    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }

    Scaffold (
        bottomBar = {
            BottomAppBar(

            ) {
                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Home
                        navigationController.navigate(Screens.Home.screen){
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ){
                    Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(26.dp),
                        tint = if(selected.value== Icons.Default.Home) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.tertiary)
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.CompassCalibration
                        navigationController.navigate(Screens.Compass.screen){
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ){
                    Icon(Icons.Default.CompassCalibration, contentDescription = null, modifier = Modifier.size(26.dp),
                        tint = if(selected.value== Icons.Default.CompassCalibration) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.tertiary)
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.LinearScale
                        navigationController.navigate(Screens.Level.screen){
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ){
                    Icon(Icons.Default.LinearScale, contentDescription = null, modifier = Modifier.size(26.dp),
                        tint = if(selected.value== Icons.Default.LinearScale) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.tertiary)
                }

                IconButton(
                    onClick = {
                        selected.value = Icons.Default.DirectionsWalk
                        navigationController.navigate(Screens.Steps.screen){
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ){
                    Icon(Icons.Default.DirectionsWalk, contentDescription = null, modifier = Modifier.size(26.dp),
                        tint = if(selected.value== Icons.Default.DirectionsWalk) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.tertiary)
                }
            }
        }
    ){
        paddingValues ->
        NavHost(navController = navigationController, startDestination = Screens.Home.screen,
            modifier = Modifier.padding(paddingValues)){
            composable(Screens.Home.screen){Home() }
            composable(Screens.Compass.screen){ val compassViewModel: CompassViewModel = viewModel()
                Compass(viewModel = compassViewModel)}
            composable(Screens.Level.screen){Level()}
            composable(Screens.Steps.screen){Steps()}

        }
    }
}


@Preview
@Composable
fun MyBottomBarPreview(){
    WandererTheme {
        MyBottomAppBar()
    }
}

