package com.example.chessapp.chessboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChessboardViewModel : ViewModel() {
    private val _boardSize = MutableLiveData<Int>()
    val boardSize: LiveData<Int> get() = _boardSize

    private val _moves = MutableLiveData<Int>()
    val moves: LiveData<Int> get() = _moves

    private val _selectedTile = MutableLiveData<Pair<Int, Int>?>()
    val selectedTile: LiveData<Pair<Int, Int>?> get() = _selectedTile

    fun setBoardSize(size: Int) {
        _boardSize.value = size
    }

    fun setMoves(moves: Int) {
        _moves.value = moves
    }

    fun onTileClicked(row: Int, col: Int) {
        _selectedTile.value = Pair(row, col)
    }
}
