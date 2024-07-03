package com.example.chessapp.chessboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private val _paths = MutableLiveData<List<List<Position>>>()
    val paths: LiveData<List<List<Position>>> get() = _paths

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

    fun findPaths() {
        val start = _startPosition.value ?: return
        val end = _endPosition.value ?: return
        val moves = _moves.value ?: return
        val boardSize = _boardSize.value ?: return // Get the board size

        viewModelScope.launch {
            val pathfinder = KnightPathfinder(boardSize) // Pass the boardSize to KnightPathfinder
            val resultPaths = withContext(Dispatchers.IO) {
                pathfinder.findPaths(Position(start.first, start.second, boardSize), Position(end.first, end.second, boardSize), moves)
            }
            if (resultPaths.isEmpty()) {
                showNoSolutionMessage()
            } else {
                _paths.value = resultPaths
            }
        }
    }

    private fun showNoSolutionMessage() {
        // Notify the UI that no paths were found
        _invalidSelection.value = false
        _paths.value = emptyList()
    }

    fun clearPaths() {
        _paths.value = emptyList()
    }
}


