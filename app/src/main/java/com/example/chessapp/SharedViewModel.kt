package com.example.chessapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _boardSize = MutableLiveData<Int>().apply { value = 8 }
    val boardSize: LiveData<Int> get() = _boardSize

    private val _moves = MutableLiveData<Int>().apply { value = 3 }
    val moves: LiveData<Int> get() = _moves

    fun updateBoardSize(size: Int) {
        _boardSize.value = size
    }

    fun updateMoves(moves: Int) {
        _moves.value = moves
    }
}
