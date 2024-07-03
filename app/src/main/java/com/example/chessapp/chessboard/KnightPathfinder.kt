package com.example.chessapp.chessboard

data class Position(val row: Int, val col: Int, val boardSize: Int) {
    fun isValidPosition(): Boolean {
        return row in 0 until boardSize && col in 0 until boardSize
    }
}


class KnightPathfinder(private val boardSize: Int) {

    // Update the findPaths method to use boardSize
    fun findPaths(start: Position, end: Position, maxMoves: Int): List<List<Position>> {
        val paths = mutableListOf<List<Position>>()
        val visited = mutableSetOf<Position>()

        fun dfs(currentPosition: Position, currentPath: List<Position>, remainingMoves: Int) {
            if (currentPosition == end) {
                paths.add(currentPath)
                return
            }
            if (remainingMoves == 0) return

            visited.add(currentPosition)

            for (move in KnightMoves) {
                val newRow = currentPosition.row + move.first
                val newCol = currentPosition.col + move.second
                val newPosition = Position(newRow, newCol, boardSize)

                if (newPosition.isValidPosition() && newPosition !in visited) {
                    dfs(newPosition, currentPath + newPosition, remainingMoves - 1)
                }
            }

            visited.remove(currentPosition)
        }

        dfs(start, listOf(start), maxMoves)
        return paths
    }

    private val KnightMoves = listOf(
        Pair(2, 1), Pair(2, -1), Pair(-2, 1), Pair(-2, -1),
        Pair(1, 2), Pair(1, -2), Pair(-1, 2), Pair(-1, -2)
    )
}