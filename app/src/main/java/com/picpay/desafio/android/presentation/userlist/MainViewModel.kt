package com.picpay.desafio.android.presentation.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.domain.model.User
import com.picpay.desafio.android.domain.repository.UsersRepository
import com.picpay.desafio.android.presentation.utils.UiState
import kotlinx.coroutines.launch

class MainViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<List<User>>>()
    val uiState: LiveData<UiState<List<User>>> get() = _uiState

    fun fetchUsers() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val usersList = usersRepository.getUsers()
                _uiState.postValue(UiState.Success(usersList))
            } catch (e: Exception) {
                _uiState.postValue(UiState.Failure(e))
            }
        }
    }

    fun refreshUsers() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val usersList = usersRepository.refreshUsers()
                _uiState.postValue(UiState.Success(usersList))
            } catch (e: Exception) {
                _uiState.postValue(UiState.Failure(e))
            }
        }
    }
}
