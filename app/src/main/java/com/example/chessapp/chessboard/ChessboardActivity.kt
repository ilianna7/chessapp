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
import com.example.chessapp.utils.PreferencesManager

class ChessboardActivity : AppCompatActivity() {

    private val viewModel: ChessboardViewModel by viewModels { ChessboardViewModelFactory(application) }
    private lateinit var pathAdapter: PathAdapter
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chessboard)

        // Initialize the PreferencesManager
        preferencesManager = PreferencesManager(this)

        // Retrieve data from shared preferences
        val boardSize = preferencesManager.getBoardSize()
        val moves = preferencesManager.getMoves()
        val startRow = preferencesManager.getStartRow()
        val startCol = preferencesManager.getStartCol()
        val endRow = preferencesManager.getEndRow()
        val endCol = preferencesManager.getEndCol()

        // Ensure the data is valid
        if (boardSize <= 0 || moves < 0) {
            throw IllegalStateException("Invalid board size or moves value")
        }

        // Bind the TextView to show board size & moves
        val boardTextView: TextView = findViewById(R.id.boardTextView)
        boardTextView.text = getString(R.string.board_size_text, boardSize)

        val movesTextView: TextView = findViewById(R.id.movesTextView)
        movesTextView.text = getString(R.string.moves_text, moves)

        // Set up the ViewModel with the data
        viewModel.setBoardSize(boardSize)
        viewModel.setMoves(moves)

        // Set start and end positions if available
        if (startRow != -1 && startCol != -1) {
            viewModel.setStartPosition(startRow, startCol)
        }
        if (endRow != -1 && endCol != -1) {
            viewModel.setEndPosition(endRow, endCol)
        }

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
        viewModel.startPosition.observe(this) { position ->
            position?.let {
                // Convert Pair<Int, Int> to Position
                val positionObj = Position(it.first, it.second, boardSize)
                // Update UI for start position
                chessboardView.updateTileColor(positionObj, R.color.blue)
            }
        }

        // Observe the endPosition LiveData
        viewModel.endPosition.observe(this) { position ->
            position?.let {
                // Convert Pair<Int, Int> to Position
                val positionObj = Position(it.first, it.second, boardSize)
                // Update UI for end position
                chessboardView.updateTileColor(positionObj, R.color.yellow)
            }
        }

        // Observe the invalidSelection LiveData
        viewModel.invalidSelection.observe(this) { isInvalid ->
            if (isInvalid) {
                showInvalidSelectionMessage()
            }
        }

        // Observe the paths LiveData
        viewModel.paths.observe(this) { paths ->
            if (paths.isNotEmpty()) {
                noPathsTextView.visibility = TextView.GONE
                pathsRecyclerView.visibility = RecyclerView.VISIBLE
                pathAdapter.updatePaths(paths)
            } else {
                noPathsTextView.visibility = TextView.VISIBLE
                pathsRecyclerView.visibility = RecyclerView.GONE
            }
        }

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

        // Load saved data
        viewModel.loadSavedData()

        // Clear dependent data when needed (for example, on starting a new game)
        // preferencesManager.clearDependentData() // Uncomment this line when you need to clear dependent data
    }

    private fun showInvalidSelectionMessage() {
        // Show a message to the user that the start and end positions cannot be the same
        Toast.makeText(this, R.string.show_invalid_selection_message, Toast.LENGTH_SHORT).show()
    }
}
