package com.example.chessapp.chessboard

import android.content.Context
import android.util.AttributeSet
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import com.example.chessapp.R

class ChessboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : GridLayout(context, attrs, defStyleAttr) {

    private var viewModel: ChessboardViewModel? = null

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
    }

    private fun setupBoard(size: Int) {
        removeAllViews()
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
                    setBackgroundColor(
                        if ((row + col) % 2 == 0) {
                            ContextCompat.getColor(context, R.color.white) // White tile
                        } else {
                            ContextCompat.getColor(context, R.color.black) // Black tile
                        }
                    )
                    setOnClickListener {
                        viewModel?.onTileClicked(row, col)
                    }
                }
                addView(button)
            }
        }
    }
}
