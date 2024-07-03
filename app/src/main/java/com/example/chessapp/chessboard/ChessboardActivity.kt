package com.example.chessapp.chessboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.chessapp.MainActivity
import com.example.chessapp.R

class ChessboardActivity : AppCompatActivity() {

    private val viewModel: ChessboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chessboard)

        // Retrieve data from the Intent
        val boardSize = intent.getIntExtra("EXTRA_BOARD_SIZE", 8)
        val moves = intent.getIntExtra("EXTRA_MOVES", 3)

        // Bind the TextView to show board size
        val boardTextView: TextView = findViewById(R.id.boardTextView)
        boardTextView.text = getString(R.string.board_size_text, boardSize)

        // Set up the ViewModel with the data
        viewModel.setBoardSize(boardSize)
        viewModel.setMoves(moves)

        // Initialize the chessboard view
        val chessboardView: ChessboardView = findViewById(R.id.chessboard)
        chessboardView.setViewModel(viewModel)

        // Set up the Back to Main button
        val backButton: Button = findViewById(R.id.btn_back_to_main)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Observe the selectedTile LiveData
        viewModel.selectedTile.observe(this, Observer { tile ->
            tile?.let {
                // Handle tile click event
                val (row, col) = it
                // Implement tile click behavior here
            }
        })
    }
}
