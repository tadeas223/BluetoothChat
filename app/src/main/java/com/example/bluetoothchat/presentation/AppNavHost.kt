package com.example.bluetoothchat.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bluetoothchat.presentation.add_user.AddUserView
import com.example.bluetoothchat.presentation.add_user.AddUserViewModel
import com.example.bluetoothchat.presentation.chat.ChatView
import com.example.bluetoothchat.presentation.contacts.ContactsView

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = "contacts") {
        composable("contacts") {
            ContactsView(
                onAddUser = {
                    navController.navigate("addUser")
                },
                onContactSelect = { id ->
                    navController.navigate("chat/$id")
                }

            )
        }

        composable("chat/{id}",
            arguments = listOf(
                navArgument("id" ) {
                    type = NavType.IntType
                }
            )
        ) { entry ->
            val id = entry.arguments?.getInt("id")
            if(id == null) {
                navController.popBackStack()
            }

            ChatView(id!!,
                onBack = {

            })
        }

        composable("addUser") {
            AddUserView( onBack = {
                navController.popBackStack()
            })
        }
    }
}
