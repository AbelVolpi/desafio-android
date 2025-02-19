package com.picpay.desafio.android.presentation.userlist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.databinding.ActivityMainBinding
import com.picpay.desafio.android.presentation.utils.UiState
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
    private lateinit var userListAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
        setupSwipeRefresh()

        if (savedInstanceState == null) {
            viewModel.fetchUsers()
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshUsers()
        }
    }

    private fun setupRecyclerView() {
        if (!::userListAdapter.isInitialized) {
            userListAdapter = UserListAdapter()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userListAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.userListProgressBar.visibility = View.VISIBLE
                }

                is UiState.Success -> {
                    binding.userListProgressBar.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                    userListAdapter.userList = state.data
                }

                is UiState.Failure -> {
                    binding.userListProgressBar.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(
                        this,
                        "Erro: ${state.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
