package com.example.chessapp.homescreen.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.chessapp.homescreen.viewmodels.HomeViewModel
import com.example.chessapp.MainActivity
import com.example.chessapp.R
import com.example.chessapp.homescreen.viewmodels.SharedViewModel
import com.example.chessapp.chessboard.ChessboardActivity
import com.example.chessapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        sharedViewModel.boardSize.observe(viewLifecycleOwner) { size ->
            updateBoardSizeTextView(size)
        }
        sharedViewModel.moves.observe(viewLifecycleOwner) { moves ->
            updateMovesTextView(moves)
        }

        // Navigate to InputSizeFragment
        binding.btnInputSize.setOnClickListener {
            viewModel.onSizeInputButton(findNavController())
        }

        // Navigate to InputMovesFragment
        binding.btnMoves.setOnClickListener {
            viewModel.onSizeMovesButton(findNavController())
        }

        // Exit the application
        binding.btnExit.setOnClickListener {
            (activity as? MainActivity)?.let {
                viewModel.onExitButtonClicked(it)
            }
        }

        binding.btnStart.setOnClickListener {
            val boardSize = sharedViewModel.boardSize.value ?: 0
            val moves = sharedViewModel.moves.value ?: 0

            val intent = Intent(requireContext(), ChessboardActivity::class.java).apply {
                putExtra("EXTRA_BOARD_SIZE", boardSize)
                putExtra("EXTRA_MOVES", moves)
            }

            startActivity(intent)
        }

        return binding.root
    }

    private fun updateBoardSizeTextView(size: Int) {
        binding.textViewBoardSize.text = getString(R.string.board_size_text, size)
    }

    private fun updateMovesTextView(moves: Int) {
        binding.textViewMoves.text = getString(R.string.moves_text, moves)
    }
}
