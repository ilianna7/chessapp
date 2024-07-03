package com.example.chessapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.example.chessapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using data binding
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Get the ViewModel
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Bind the ViewModel to the layout
        binding.viewModel = viewModel

        // Set the lifecycle owner so LiveData can automatically update the UI
        binding.lifecycleOwner = viewLifecycleOwner

        // Observe the exit event
        observeViewModel()

        return binding.root
    }

    private fun observeViewModel() {
        viewModel.exitEvent.observe(viewLifecycleOwner, Observer { shouldExit ->
            if (shouldExit) {
                activity?.finishAffinity()
                viewModel.onExitHandled()  // Reset the event
            }
        })
    }
}
