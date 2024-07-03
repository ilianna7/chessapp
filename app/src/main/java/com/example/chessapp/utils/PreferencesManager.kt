package com.example.chessapp.utils

import android.content.Context
import com.example.chessapp.chessboard.Position
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesManager(private val context: Context) {

    companion object {
        private const val PREFS_FILE = "com.example.chessapp.prefs"
        private const val KEY_BOARD_SIZE = "board_size"
        private const val KEY_MOVES = "moves"
        private const val KEY_START_ROW = "start_row"
        private const val KEY_START_COL = "start_col"
        private const val KEY_END_ROW = "end_row"
        private const val KEY_END_COL = "end_col"
        private const val KEY_PATHS = "paths"
    }

    private val prefs = context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)
    private val gson = Gson()

    // Board Size
    fun getBoardSize(): Int {
        return prefs.getInt(KEY_BOARD_SIZE, 8) // default value is 8
    }

    fun setBoardSize(boardSize: Int) {
        prefs.edit().putInt(KEY_BOARD_SIZE, boardSize).apply()
    }

    // Moves
    fun getMoves(): Int {
        return prefs.getInt(KEY_MOVES, 3) // default value is 3
    }

    fun setMoves(moves: Int) {
        prefs.edit().putInt(KEY_MOVES, moves).apply()
    }

    // Start Position
    fun getStartRow(): Int {
        return prefs.getInt(KEY_START_ROW, -1)
    }

    fun getStartCol(): Int {
        return prefs.getInt(KEY_START_COL, -1)
    }

    fun setStartPosition(row: Int, col: Int) {
        prefs.edit().putInt(KEY_START_ROW, row).putInt(KEY_START_COL, col).apply()
    }

    // End Position
    fun getEndRow(): Int {
        return prefs.getInt(KEY_END_ROW, -1)
    }

    fun getEndCol(): Int {
        return prefs.getInt(KEY_END_COL, -1)
    }

    fun setEndPosition(row: Int, col: Int) {
        prefs.edit().putInt(KEY_END_ROW, row).putInt(KEY_END_COL, col).apply()
    }

    // Paths
    fun getPaths(): List<List<Position>> {
        val pathsJson = prefs.getString(KEY_PATHS, null)
        return if (pathsJson != null) {
            val type = object : TypeToken<List<List<Position>>>() {}.type
            gson.fromJson(pathsJson, type)
        } else {
            emptyList()
        }
    }

    fun setPaths(paths: List<List<Position>>) {
        val pathsJson = gson.toJson(paths)
        prefs.edit().putString(KEY_PATHS, pathsJson).apply()
    }

    // Clear dependent data
    fun clearDependentData() {
        prefs.edit().remove(KEY_START_ROW)
            .remove(KEY_START_COL)
            .remove(KEY_END_ROW)
            .remove(KEY_END_COL)
            .remove(KEY_PATHS)
            .apply()
    }
}
