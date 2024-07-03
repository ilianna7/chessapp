package com.example.chessapp.homescreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.chessapp.homescreen.viewmodels.InputSizeViewModel
import com.example.chessapp.R
import com.example.chessapp.SharedViewModel
import com.example.chessapp.databinding.FragmentInputSizeBinding

class InputSizeFragment : Fragment() {

    private lateinit var binding: FragmentInputSizeBinding
    private val viewModel: InputSizeViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInputSizeBinding.inflate(inflater, container, false)

        // Bind the ViewModel to the Fragment
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Set up Cancel button click listener using ViewModel
        binding.btnCancel.setOnClickListener {
            viewModel.onCancelButtonClicked(findNavController())
        }

        // Set up Confirm button click listener
        binding.btnConfirm.setOnClickListener {
            val sizeText = binding.editTextSize.text.toString()
            val boardSize = sizeText.toIntOrNull()

            if (boardSize == null) {
                Toast.makeText(requireContext(), R.string.invalid_number, Toast.LENGTH_SHORT).show()
            } else if (boardSize in 6..16) {
                // Update the board size in the shared ViewModel
                sharedViewModel.updateBoardSize(boardSize)
                viewModel.onConfirmButtonClicked(boardSize, findNavController())
            } else {
                Toast.makeText(requireContext(), R.string.size_out_of_range, Toast.LENGTH_SHORT).show()
            }
        }


        return binding.root
    }
}
