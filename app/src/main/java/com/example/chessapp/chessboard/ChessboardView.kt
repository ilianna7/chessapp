package com.example.chessapp.chessboard

import android.content.Context
import android.util.AttributeSet
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.chessapp.R

class ChessboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : GridLayout(context, attrs, defStyleAttr) {

    private var viewModel: ChessboardViewModel? = null
    private val buttons = mutableMapOf<Pair<Int, Int>, androidx.appcompat.widget.AppCompatButton>()

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        rowCount = 0
        columnCount = 0
    }

    fun setViewModel(viewModel: ChessboardViewModel) {
        this.viewModel = viewModel
        viewModel.boardSize.observeForever { size ->
            setupBoard(size)
        }
        viewModel.startPosition.observeForever { position ->
            position?.let { updateTileColor(it, R.color.blue) }
        }
        viewModel.endPosition.observeForever { position ->
            position?.let { updateTileColor(it, R.color.yellow) }
        }
        viewModel.previousStartPosition.observeForever { position ->
            position?.let { resetTileColor(it) }
        }
        viewModel.previousEndPosition.observeForever { position ->
            position?.let { resetTileColor(it) }
        }
        viewModel.invalidSelection.observeForever { isInvalid ->
            if (isInvalid) {
                // Show an error message or handle invalid selection case
                showInvalidSelectionMessage()
            }
        }
    }

    private fun setupBoard(size: Int) {
        removeAllViews()
        buttons.clear()
        rowCount = size
        columnCount = size

        for (row in 0 until size) {
            for (col in 0 until size) {
                val button = androidx.appcompat.widget.AppCompatButton(context).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 0
                        rowSpec = GridLayout.spec(row, 1f)
                        columnSpec = GridLayout.spec(col, 1f)
                    }
                    text = ""
                    setBackgroundColor(getTileColor(row, col))
                    setOnClickListener {
                        viewModel?.onTileClicked(row, col)
                    }
                }
                addView(button)
                buttons[Pair(row, col)] = button
            }
        }
    }

    private fun getTileColor(row: Int, col: Int): Int {
        return if ((row + col) % 2 == 0) {
            ContextCompat.getColor(context, R.color.white)
        } else {
            ContextCompat.getColor(context, R.color.black)
        }
    }

    private fun updateTileColor(position: Pair<Int, Int>, colorResId: Int) {
        buttons[position]?.setBackgroundColor(ContextCompat.getColor(context, colorResId))
    }

    private fun resetTileColor(position: Pair<Int, Int>) {
        val (row, col) = position
        buttons[position]?.setBackgroundColor(getTileColor(row, col))
    }

    private fun showInvalidSelectionMessage() {
        // Show a message to the user that the start and end positions cannot be the same
        // For example, using a Toast:
        Toast.makeText(context, "Start and end positions cannot be the same", Toast.LENGTH_SHORT).show()
    }
}
