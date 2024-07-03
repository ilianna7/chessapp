package com.example.chessapp.chessboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chessapp.utils.PreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChessboardViewModel(application: Application) : AndroidViewModel(application) {

    private val preferencesManager: PreferencesManager = PreferencesManager(application.applicationContext)

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

    private val _clearPurplePaths = MutableLiveData<Unit>()
    val clearPurplePaths: LiveData<Unit> get() = _clearPurplePaths

    init {
        _currentMode.value = Mode.START_POSITION
        loadSavedData()
    }

    fun setBoardSize(size: Int) {
        _boardSize.value = size
        preferencesManager.setBoardSize(size)
    }

    fun setMoves(moves: Int) {
        _moves.value = moves
        preferencesManager.setMoves(moves)
    }

    fun setStartPosition(row: Int, col: Int) {
        preferencesManager.setStartPosition(row, col)
        _startPosition.value = Pair(row, col)
    }

    fun setEndPosition(row: Int, col: Int) {
        _endPosition.value = Pair(row, col)
        preferencesManager.setEndPosition(row, col)
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
                    setStartPosition(row, col)
                    clearPaths()  // Clear paths when start position changes
                }
            }
            Mode.END_POSITION -> {
                if (tile == _startPosition.value) {
                    _invalidSelection.value = true
                } else {
                    _invalidSelection.value = false
                    _previousEndPosition.value = _endPosition.value
                    setEndPosition(row, col)
                    clearPaths()  // Clear paths when end position changes
                }
            }
            null -> throw IllegalStateException("Mode should never be null")
        }
    }

    fun setMode(mode: Mode) {
        _currentMode.value = mode
    }

    fun findPaths() {
        val start = _startPosition.value ?: return
        val end = _endPosition.value ?: return
        val moves = _moves.value ?: return
        val boardSize = _boardSize.value ?: return

        viewModelScope.launch {
            val pathfinder = KnightPathfinder(boardSize)
            var resultPaths = withContext(Dispatchers.IO) {
                pathfinder.findPaths(Position(start.first, start.second, boardSize), Position(end.first, end.second, boardSize), moves)
            }
            if (resultPaths.isEmpty()) {
                _invalidSelection.value = false
                resultPaths = listOf(listOf(Position(-1, -1, boardSize)))
            }
            _paths.value = resultPaths
            preferencesManager.setPaths(resultPaths)

        }
    }

    fun clearPaths() {
        _paths.value = emptyList()
        preferencesManager.clearPathData()
        _clearPurplePaths.value = Unit  // Notify to clear purple tiles
    }

    fun loadSavedData() {
        _boardSize.value = preferencesManager.getBoardSize()
        _moves.value = preferencesManager.getMoves()
        val startRow = preferencesManager.getStartRow()
        val startCol = preferencesManager.getStartCol()
        if (startRow != -1 && startCol != -1) {
            _startPosition.value = Pair(startRow, startCol)
        }
        val endRow = preferencesManager.getEndRow()
        val endCol = preferencesManager.getEndCol()
        if (endRow != -1 && endCol != -1) {
            _endPosition.value = Pair(endRow, endCol)
        }
        _paths.value = preferencesManager.getPaths()
    }
}
