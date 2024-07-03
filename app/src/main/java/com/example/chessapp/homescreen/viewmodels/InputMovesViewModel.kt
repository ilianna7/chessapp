package com.example.chessapp.homescreen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.chessapp.R

class InputMovesViewModel : ViewModel() {

    fun onCancelButtonClicked(navController: NavController) {
        navController.navigate(R.id.action_inputMovesFragment_to_homeFragment)
    }

    fun onConfirmButtonClicked(boardSize: Int, navController: NavController) {
        if (boardSize in 1..10) {
            // Navigate back to HomeFragment
            navController.navigate(R.id.action_inputMovesFragment_to_homeFragment)
        } else {
            // Show an error message if the size is out of the acceptable range
            // Display an error message for invalid size
        }
    }
}
