package com.example.chessapp.chessboard

import android.content.Context
import android.graphics.drawable.ColorDrawable
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

    fun updateTileColor(position: Pair<Int, Int>, colorResId: Int) {
        buttons[position]?.setBackgroundColor(ContextCompat.getColor(context, colorResId))
    }

    private fun resetTileColor(position: Pair<Int, Int>) {
        val (row, col) = position
        buttons[position]?.setBackgroundColor(getTileColor(row, col))
    }

    fun displayPath(path: List<Position>) {
        path.forEachIndexed { index, pos ->
            val color = when (index) {
                0 -> R.color.blue    // Start position
                path.size - 1 -> R.color.yellow  // End position
                else -> R.color.purple  // Path positions
            }
            updateTileColor(Pair(pos.row, pos.col), color)
        }
    }

    fun clearPurplePath() {
        val purpleColor = ContextCompat.getColor(context, R.color.purple)
        // Restore purple path tiles to their original colors, preserving the start and end positions
        buttons.forEach { (position, button) ->
            val (row, col) = position
            if (button.backgroundColor() == purpleColor) {
                // Do not change the colors of start and end positions
                if (position != viewModel?.startPosition?.value && position != viewModel?.endPosition?.value) {
                    button.setBackgroundColor(getTileColor(row, col))
                }
            }
        }
    }

    private fun androidx.appcompat.widget.AppCompatButton.backgroundColor(): Int {
        return (background as? ColorDrawable)?.color ?: 0
    }


    private fun showInvalidSelectionMessage() {
        // Show a message to the user that the start and end positions cannot be the same
        Toast.makeText(context, R.string.show_invalid_selection_message, Toast.LENGTH_SHORT).show()
    }
}
