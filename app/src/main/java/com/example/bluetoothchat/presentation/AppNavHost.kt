package com.example.bluetoothchat.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bluetoothchat.presentation.add_user.AddUserView
import com.example.bluetoothchat.presentation.add_user.AddUserViewModel
import com.example.bluetoothchat.presentation.contacts.ContactsView

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = "contacts") {
        composable("contacts") {
            ContactsView( onAddUser = {
                navController.navigate("addUser")
            })
        }

        composable("addUser") {
            AddUserView( onBack = {
                navController.popBackStack()
            })
        }
    }
}
