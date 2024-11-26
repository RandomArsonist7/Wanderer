package com.example.wanderer

sealed class Screens(
    val screen: String){
    data object Home: Screens("Home")
    data object Compass: Screens("Compass")
    data object Map: Screens("Map")
    data object Level: Screens("Level")
    data object Steps: Screens("Steps")


}
