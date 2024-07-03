package com.example.chessapp.homescreen.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chessapp.utils.PreferencesManager

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager: PreferencesManager = PreferencesManager(application.applicationContext)

    private val _boardSize = MutableLiveData<Int>()
    val boardSize: LiveData<Int> get() = _boardSize

    private val _moves = MutableLiveData<Int>()
    val moves: LiveData<Int> get() = _moves

    init {
        loadPreferences()
    }

    fun updateBoardSize(size: Int) {
        _boardSize.value = size
        clearDependentData()
        savePreferences()
    }

    fun updateMoves(moves: Int) {
        _moves.value = moves
        clearDependentData()
        savePreferences()
    }

    private fun savePreferences() {
        preferencesManager.setBoardSize(_boardSize.value ?: 8)  // Default to 8
        preferencesManager.setMoves(_moves.value ?: 3)  // Default to 3
    }

    private fun loadPreferences() {
        _boardSize.value = preferencesManager.getBoardSize()
        _moves.value = preferencesManager.getMoves()
    }

    private fun clearDependentData() {
        preferencesManager.clearDependentData()
    }
}
