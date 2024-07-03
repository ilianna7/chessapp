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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chessapp.MainActivity
import com.example.chessapp.R


class ChessboardActivity : AppCompatActivity() {

    private val viewModel: ChessboardViewModel by viewModels()
    private lateinit var pathAdapter: PathAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chessboard)

        // Retrieve data from the Intent
        val boardSize = intent.getIntExtra("EXTRA_BOARD_SIZE", 8)
        val moves = intent.getIntExtra("EXTRA_MOVES", 3)

        // Bind the TextView to show board size & moves
        val boardTextView: TextView = findViewById(R.id.boardTextView)
        boardTextView.text = getString(R.string.board_size_text, boardSize)

        val movesTextView: TextView = findViewById(R.id.movesTextView)
        movesTextView.text = getString(R.string.moves_text, moves)

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

        // Initialize RecyclerView for paths
        val pathsRecyclerView: RecyclerView = findViewById(R.id.pathsRecyclerView)
        val noPathsTextView: TextView = findViewById(R.id.noPathsTextView)

        pathAdapter = PathAdapter(emptyList())
        pathsRecyclerView.adapter = pathAdapter
        pathsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Add item decoration for horizontal RecyclerView
        pathsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                pathsRecyclerView.context,
                DividerItemDecoration.HORIZONTAL
            )
        )

        // Observe the startPosition LiveData
        viewModel.startPosition.observe(this, Observer { position ->
            position?.let {
                // Convert Pair<Int, Int> to Position
                val positionObj = Position(it.first, it.second, boardSize)
                // Update UI for start position
                chessboardView.updateTileColor(positionObj, R.color.blue)
            }
        })

        // Observe the endPosition LiveData
        viewModel.endPosition.observe(this, Observer { position ->
            position?.let {
                // Convert Pair<Int, Int> to Position
                val positionObj = Position(it.first, it.second, boardSize)
                // Update UI for end position
                chessboardView.updateTileColor(positionObj, R.color.yellow)
            }
        })


        // Observe the invalidSelection LiveData
        viewModel.invalidSelection.observe(this, Observer { isInvalid ->
            if (isInvalid) {
                showInvalidSelectionMessage()
            }
        })

        // Observe the paths LiveData
        viewModel.paths.observe(this, Observer { paths ->
            if (paths.isNotEmpty()) {
                noPathsTextView.visibility = TextView.GONE
                pathsRecyclerView.visibility = RecyclerView.VISIBLE
                pathAdapter.updatePaths(paths)
            } else {
                noPathsTextView.visibility = TextView.VISIBLE
                pathsRecyclerView.visibility = RecyclerView.GONE
            }
        })

        // Set up the Find Path button
        val findPathButton: Button = findViewById(R.id.btnFindPath)
        findPathButton.setOnClickListener {
            // Clear previous purple paths
            chessboardView.clearPurplePath()
            viewModel.findPaths()
        }

        // Handle path item clicks
        pathAdapter.setOnItemClickListener { path ->
            // Clear the purple path on the chessboard and display the clicked path
            chessboardView.clearPurplePath()
            chessboardView.displayPath(path)
        }
    }

    private fun showInvalidSelectionMessage() {
        // Show a message to the user that the start and end positions cannot be the same
        Toast.makeText(this, R.string.show_invalid_selection_message, Toast.LENGTH_SHORT).show()
    }
}
