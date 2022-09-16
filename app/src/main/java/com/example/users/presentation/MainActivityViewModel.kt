package com.example.users.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.users.data.model.User
import com.example.users.domain.repository.IUserRepository
import com.example.users.util.Resource
import com.example.users.util.exhausted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


data class UsersUiState(
    val students: List<User> = emptyList(),
    val loading: Boolean = false,
)

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(UsersUiState(loading = true))
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<String>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getStudents()
    }

    /*Get User List*/
    private fun getStudents() {
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            userRepository.getUsers().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                loading = false,
                                students = result.data,
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                loading = false,
                                students = result.data ?: emptyList(),
                            )
                        }

                        _uiEvent.send(result.message)

                    }
                    is Resource.Loading -> {
                        _uiState.update { it.copy(loading = true) }
                    }
                }.exhausted
            }
        }
    }


    fun addUser(user: User) {
        viewModelScope.launch {
            userRepository.addUser(user)
        }
    }

    fun saveImage(id: Int, imagePath: Uri) {
        val imageString = imagePath.toString()
        viewModelScope.launch {
            userRepository.saveImage(id, imageString)
        }
    }
}