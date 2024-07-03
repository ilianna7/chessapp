package com.example.chessapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.example.chessapp.databinding.ActivityMainBinding
import com.example.chessapp.homescreen.viewmodels.HomeViewModel

class MainActivity : AppCompatActivity() {

    // Get the HomeViewModel instance
    val homeViewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(3000)
        installSplashScreen()
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the NavHostFragment and NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Set up the NavController with the HomeViewModel
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Example of setting up actions based on the destination
        }
    }

    fun exitApplication() {
        finishAffinity()  // Close all activities and exit the app
    }
}
