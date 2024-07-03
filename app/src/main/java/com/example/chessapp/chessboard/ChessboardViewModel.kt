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

    private val _startPosition = MutableLiveData<Pair<Int, Int>?>()
    val startPosition: LiveData<Pair<Int, Int>?> get() = _startPosition

    private val _endPosition = MutableLiveData<Pair<Int, Int>?>()
    val endPosition: LiveData<Pair<Int, Int>?> get() = _endPosition

    private val _previousStartPosition = MutableLiveData<Pair<Int, Int>?>()
    val previousStartPosition: LiveData<Pair<Int, Int>?> get() = _previousStartPosition

    private val _previousEndPosition = MutableLiveData<Pair<Int, Int>?>()
    val previousEndPosition: LiveData<Pair<Int, Int>?> get() = _previousEndPosition

    private val _currentMode = MutableLiveData<Mode>()
    val currentMode: LiveData<Mode> get() = _currentMode

    private val _invalidSelection = MutableLiveData<Boolean>()
    val invalidSelection: LiveData<Boolean> get() = _invalidSelection

    init {
        _currentMode.value = Mode.START_POSITION
    }

    fun setBoardSize(size: Int) {
        _boardSize.value = size
    }

    fun setMoves(moves: Int) {
        _moves.value = moves
    }

    fun onTileClicked(row: Int, col: Int) {
        val tile = Pair(row, col)
        _selectedTile.value = tile
        when (_currentMode.value) {
            Mode.START_POSITION -> {
                if (tile == _endPosition.value) {
                    _invalidSelection.value = true
                } else {
                    _invalidSelection.value = false
                    _previousStartPosition.value = _startPosition.value
                    _startPosition.value = tile
                }
            }
            Mode.END_POSITION -> {
                if (tile == _startPosition.value) {
                    _invalidSelection.value = true
                } else {
                    _invalidSelection.value = false
                    _previousEndPosition.value = _endPosition.value
                    _endPosition.value = tile
                }
            }

            null -> TODO()
        }
    }

    fun setMode(mode: Mode) {
        _currentMode.value = mode
    }
}
