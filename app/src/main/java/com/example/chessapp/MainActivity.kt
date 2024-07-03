package com.example.chessapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.example.chessapp.databinding.ActivityMainBinding
import com.example.chessapp.homescreen.viewmodels.HomeViewModel
import com.example.chessapp.homescreen.viewmodels.SharedViewModel

class MainActivity : AppCompatActivity() {

    // Get the HomeViewModel instance
    val homeViewModel: HomeViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Install the splash screen
        val splashScreen = installSplashScreen()

        // Inflate the layout
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // This is the first creation of the activity, apply the splash screen delay
            splashScreen.setKeepOnScreenCondition {
                // Keep the splash screen visible
                true
            }

            // Set up a delay
            val delayMillis = 1000L
            val handler = android.os.Handler(mainLooper)
            handler.postDelayed({
                splashScreen.setKeepOnScreenCondition { false } // Dismiss the splash screen
                setUpNavHostFragment() // Continue with setting up the NavHostFragment
            }, delayMillis)
        } else {
            // If there's a saved instance state, skip the splash screen delay
            splashScreen.setKeepOnScreenCondition { false } // Immediately dismiss the splash screen
            setUpNavHostFragment() // Continue with setting up the NavHostFragment
        }
    }

    private fun setUpNavHostFragment() {
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
