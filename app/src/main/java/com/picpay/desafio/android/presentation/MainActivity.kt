package com.picpay.desafio.android.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
        viewModel.fetchUsers()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = UserListAdapter()
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.userListProgressBar.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    binding.userListProgressBar.visibility = View.GONE
                    (binding.recyclerView.adapter as UserListAdapter).userList = state.data
                }

                is UiState.Failure -> {
                    binding.userListProgressBar.visibility = View.GONE
                    Toast.makeText(this, "Erro: ${state.exception.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
