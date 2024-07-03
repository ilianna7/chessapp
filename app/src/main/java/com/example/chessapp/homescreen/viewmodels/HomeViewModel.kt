package com.example.chessapp.homescreen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.chessapp.MainActivity
import com.example.chessapp.R

class HomeViewModel : ViewModel() {

    fun onSizeInputButton(navController: NavController) {
        navController.navigate(R.id.action_homeFragment_to_inputSizeFragment)
    }

    fun onSizeMovesButton(navController: NavController) {
        navController.navigate(R.id.action_homeFragment_to_inputMovesFragment)
    }

    fun onExitButtonClicked(activity: MainActivity) {
        activity.exitApplication()
    }
}
