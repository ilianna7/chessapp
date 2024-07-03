package com.example.chessapp.homescreen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.chessapp.R

class InputSizeViewModel : ViewModel() {

    fun onCancelButtonClicked(navController: NavController) {
        navController.navigate(R.id.action_inputSizeFragment_to_homeFragment)
    }

    fun onConfirmButtonClicked(boardSize: Int, navController: NavController) {
        if (boardSize in 6..16) {
            // Navigate back to HomeFragment
            navController.navigate(R.id.action_inputSizeFragment_to_homeFragment)
        } else {
            // Show an error message if the size is out of the acceptable range
            // Display an error message for invalid size
        }
    }
}
