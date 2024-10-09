package com.azzam.protectmyfiles.presentation.savedPasswords

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.azzam.protectmyfiles.domain.model.PasswordModel
import com.azzam.protectmyfiles.domain.repository.Repository
import com.azzam.protectmyfiles.util.Resource
import com.azzam.protectmyfiles.util.UIEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class SavedPasswordsViewModel(private val repository: Repository) : ViewModel() {
    var state by mutableStateOf(SavedPasswordsListState())
        private set

    private val _showErrorMessage = Channel<UIEvent>()
    val showErrorMessage = _showErrorMessage.receiveAsFlow()


    fun getAllSavedPasswords() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllPasswordModels().onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        state = state.copy(
                            isLoading = false
                        )
                        _showErrorMessage.trySend(UIEvent.ShowStringMessage(result.message))
                    }

                    is Resource.Loading -> {
                        state = state.copy(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {
                        result.data?.forEach {
                            Timber.v("name: ${it?.name}")
                            Timber.v("password: ${it?.password}")
                            Timber.v("username: ${it?.username}")
                        }
                        state = state.copy(
                            isLoading = false,
                            savedPasswords = result.data ?: emptyList()
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    fun insertPasswordModel(passwordModel: PasswordModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPasswordModel(
                passwordModel
            ).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        Timber.v(result.message)
                        _showErrorMessage.trySend(UIEvent.ShowStringMessage(result.message))
                    }

                    is Resource.Loading -> {
                        Timber.v("Loading")
                    }

                    is Resource.Success -> {
                        getAllSavedPasswords()
                    }
                }

            }.launchIn(this)
        }
    }

    fun deletePasswordModel(passwordModel: PasswordModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePasswordModel(passwordModel).onEach { result ->
                when (result) {
                    is Resource.Error -> {
                        Timber.v(result.message)
                        _showErrorMessage.trySend(UIEvent.ShowStringMessage(result.message))
                    }

                    is Resource.Loading -> {
                        Timber.v("Loading")
                    }

                    is Resource.Success -> {
                        getAllSavedPasswords()
                    }
                }

            }.launchIn(this)
        }
    }
}