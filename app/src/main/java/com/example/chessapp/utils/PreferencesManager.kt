package com.example.chessapp.utils

import android.content.Context
import androidx.core.content.edit
import com.example.chessapp.chessboard.Position
import java.lang.NumberFormatException

class PreferencesManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("chess_prefs", Context.MODE_PRIVATE)

    fun setBoardSize(size: Int) {
        sharedPreferences.edit {
            putInt("board_size", size)
            apply()  // Apply changes asynchronously
        }
    }

    fun getBoardSize(): Int {
        return sharedPreferences.getInt("board_size", 8)  // Default to 8 if not set
    }

    fun setMoves(moves: Int) {
        sharedPreferences.edit {
            putInt("moves", moves)
            apply()  // Apply changes asynchronously
        }
    }

    fun getMoves(): Int {
        return sharedPreferences.getInt("moves", 3)  // Default to 3 if not set
    }

    fun setStartPosition(row: Int, col: Int) {
        sharedPreferences.edit {
            putInt("start_row", row)
            putInt("start_col", col)
            apply()  // Apply changes asynchronously
        }
    }

    fun getStartRow(): Int {
        return sharedPreferences.getInt("start_row", -1)  // Default value is -1
    }

    fun getStartCol(): Int {
        return sharedPreferences.getInt("start_col", -1)  // Default value is -1
    }

    fun setEndPosition(row: Int, col: Int) {
        sharedPreferences.edit {
            putInt("end_row", row)
            putInt("end_col", col)
            apply()  // Apply changes asynchronously
        }
    }

    fun getEndRow(): Int {
        return sharedPreferences.getInt("end_row", -1)  // Default value is -1
    }

    fun getEndCol(): Int {
        return sharedPreferences.getInt("end_col", -1)  // Default value is -1
    }

    fun setPaths(paths: List<List<Position>>) {
        val pathsString = paths.joinToString(separator = ";") { path ->
            path.joinToString(separator = ",") { "${it.row}:${it.col}" }
        }
        sharedPreferences.edit {
            putString("paths", pathsString)
            apply()  // Apply changes asynchronously
        }
    }

    fun getPaths(): List<List<Position>> {
        val pathsString = sharedPreferences.getString("paths", "") ?: ""
        if (pathsString.isBlank()) return emptyList()  // Return empty list if no paths are saved

        return pathsString.split(";").map { path ->
            path.split(",").map { pos ->
                val (row, col) = pos.split(":").map { it.toIntOrNull() ?: throw NumberFormatException("Invalid path format") }
                Position(row, col, getBoardSize())
            }
        }
    }

    fun clearDependentData() {
        sharedPreferences.edit {
            remove("my_start_row")
            remove("start_col")
            remove("end_row")
            remove("end_col")
            remove("paths")
            commit()  // Ensure changes are committed
        }
    }
    fun clearPathData() {
        sharedPreferences.edit {
            remove("paths")
            commit()  // Ensure changes are committed
        }
    }
}
