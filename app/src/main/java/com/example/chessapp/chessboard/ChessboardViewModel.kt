package com.example.chessapp.chessboard

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChessboardViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences("chess_prefs", Context.MODE_PRIVATE)

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
        saveBoardSize(size)
    }

    fun setMoves(moves: Int) {
        _moves.value = moves
        saveMoves(moves)
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
                    saveStartPosition(tile)
                }
            }
            Mode.END_POSITION -> {
                if (tile == _startPosition.value) {
                    _invalidSelection.value = true
                } else {
                    _invalidSelection.value = false
                    _previousEndPosition.value = _endPosition.value
                    _endPosition.value = tile
                    saveEndPosition(tile)
                }
            }
            null -> TODO()
        }
    }

    fun setMode(mode: Mode) {
        _currentMode.value = mode
        saveCurrentMode(mode)
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
                savePaths(resultPaths)
            }
        }
    }

    private fun showNoSolutionMessage() {
        _invalidSelection.value = false
        _paths.value = emptyList()
    }

    fun clearPaths() {
        _paths.value = emptyList()
        clearSavedPaths()
    }

    // Save methods
    private fun saveBoardSize(size: Int) {
        sharedPreferences.edit().putInt("board_size", size).apply()
    }

    private fun saveMoves(moves: Int) {
        sharedPreferences.edit().putInt("moves", moves).apply()
    }

    private fun saveStartPosition(position: Pair<Int, Int>) {
        sharedPreferences.edit().putInt("start_row", position.first).putInt("start_col", position.second).apply()
    }

    private fun saveEndPosition(position: Pair<Int, Int>) {
        sharedPreferences.edit().putInt("end_row", position.first).putInt("end_col", position.second).apply()
    }

    private fun saveCurrentMode(mode: Mode) {
        sharedPreferences.edit().putString("current_mode", mode.name).apply()
    }

    private fun savePaths(paths: List<List<Position>>) {
        val gson = Gson()
        val pathsJson = gson.toJson(paths)
        sharedPreferences.edit().putString("paths", pathsJson).apply()
    }

    private fun clearSavedPaths() {
        sharedPreferences.edit().remove("paths").apply()
    }

    // Load methods
    fun loadSavedData() {
        _boardSize.value = sharedPreferences.getInt("board_size", 8)
        _moves.value = sharedPreferences.getInt("moves", 3)
        val startRow = sharedPreferences.getInt("start_row", -1)
        val startCol = sharedPreferences.getInt("start_col", -1)
        if (startRow != -1 && startCol != -1) {
            _startPosition.value = Pair(startRow, startCol)
        }
        val endRow = sharedPreferences.getInt("end_row", -1)
        val endCol = sharedPreferences.getInt("end_col", -1)
        if (endRow != -1 && endCol != -1) {
            _endPosition.value = Pair(endRow, endCol)
        }
        _currentMode.value = Mode.valueOf(sharedPreferences.getString("current_mode", Mode.START_POSITION.name) ?: Mode.START_POSITION.name)
        val pathsJson = sharedPreferences.getString("paths", null)
        if (pathsJson != null) {
            val gson = Gson()
            val type = object : TypeToken<List<List<Position>>>() {}.type
            _paths.value = gson.fromJson(pathsJson, type)
        }
    }
}
