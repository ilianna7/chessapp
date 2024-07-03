package com.example.chessapp.homescreen.fragments

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
import com.example.chessapp.SharedViewModel
import com.example.chessapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by activityViewModels()  // Initialize HomeViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        updateBoardSizeTextView(8)

        sharedViewModel.boardSize.observe(viewLifecycleOwner) { size ->
            updateBoardSizeTextView(size)
        }

        // Navigate to InputSizeFragment
        binding.btnInputSize.setOnClickListener {
            viewModel.onSizeInputButton(findNavController())
        }

        // Exit the application
        binding.btnExit.setOnClickListener {
            // Ensure the activity is of type MainActivity
            (activity as? MainActivity)?.let {
                viewModel.onExitButtonClicked(it)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }

    fun updateBoardSizeTextView(size: Int){
        binding.textViewBoardSize.text = getString(R.string.board_size_text, size)
    }
}
