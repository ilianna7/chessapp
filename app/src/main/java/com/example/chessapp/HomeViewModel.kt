package com.example.chessapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _exitEvent = MutableLiveData<Boolean>()
    val exitEvent: LiveData<Boolean> get() = _exitEvent

    // Function to handle exit button click
    fun onExitClicked() {
        _exitEvent.value = true
    }

    // Function to reset the exit event
    fun onExitHandled() {
        _exitEvent.value = false
    }
}
