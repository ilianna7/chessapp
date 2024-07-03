package com.example.chessapp.homescreen.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.content.Context

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val PREFS_NAME = "ChessAppPrefs"
        private const val KEY_BOARD_SIZE = "board_size"
        private const val KEY_MOVES = "moves"
    }

    private val _boardSize = MutableLiveData<Int>()
    val boardSize: LiveData<Int> get() = _boardSize

    private val _moves = MutableLiveData<Int>()
    val moves: LiveData<Int> get() = _moves

    init {
        loadPreferences()
    }

    fun updateBoardSize(size: Int) {
        _boardSize.value = size
        savePreferences()
    }

    fun updateMoves(moves: Int) {
        _moves.value = moves
        savePreferences()
    }

    private fun savePreferences() {
        val context = getApplication<Application>().applicationContext
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putInt(KEY_BOARD_SIZE, _boardSize.value ?: 8)  // Default to 8
            putInt(KEY_MOVES, _moves.value ?: 3)  // Default to 3
            apply()
        }
    }

    private fun loadPreferences() {
        val context = getApplication<Application>().applicationContext
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        _boardSize.value = prefs.getInt(KEY_BOARD_SIZE, 8)  // Default to 8
        _moves.value = prefs.getInt(KEY_MOVES, 3)  // Default to 3
    }
}
