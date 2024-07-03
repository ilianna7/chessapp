package com.example.chessapp.chessboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
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

        // Set up the RadioGroup for start and end positions
        val radioGroup: RadioGroup = findViewById(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioStartPosition -> viewModel.setMode(Mode.START_POSITION)
                R.id.radioEndPosition -> viewModel.setMode(Mode.END_POSITION)
            }
        }

        // Observe the startPosition LiveData
        viewModel.startPosition.observe(this, Observer { position ->
            position?.let {
                // Update UI for start position
                // Example: Highlight the start position tile
            }
        })

        // Observe the endPosition LiveData
        viewModel.endPosition.observe(this, Observer { position ->
            position?.let {
                // Update UI for end position
                // Example: Highlight the end position tile
            }
        })

        // Observe the invalidSelection LiveData
        viewModel.invalidSelection.observe(this, Observer { isInvalid ->
            if (isInvalid) {
                // Show an error message or handle invalid selection case
                showInvalidSelectionMessage()
            }
        })

        // Set up the Find Path button
        val findPathButton: Button = findViewById(R.id.btnFindPath)
        findPathButton.setOnClickListener {
            val start = viewModel.startPosition.value
            val end = viewModel.endPosition.value

            if (start == null || end == null) {
                Toast.makeText(this, R.string.set_both_positions, Toast.LENGTH_SHORT).show()
            } else {
                // Logic to find the path would go here. For now, we'll just show a success message.
                Toast.makeText(this, "Path found successfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showInvalidSelectionMessage() {
        // Show a message to the user that the start and end positions cannot be the same
        Toast.makeText(this, R.string.show_invalid_selection_message, Toast.LENGTH_SHORT).show()
    }
}
