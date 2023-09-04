package com.example.mvi_sample.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvi_sample.data.repository.MainRepository
import com.example.mvi_sample.ui.intent.MainIntent
import com.example.mvi_sample.ui.viewState.MainState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MainViewModel (
  private val repository: MainRepository
  ): ViewModel() {


   val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
   private val _state = MutableStateFlow<MainState>(MainState.Idle)
   val state: StateFlow<MainState>
   get() = _state


   private fun handleIntent() {
     viewModelScope.launch {
     userIntent.consumeAsFlow().collect {
     when (it) {
     is MainIntent.FetchUser -> fetchUser()
      }
    }
   }
 }

 private fun fetchUser() {
  viewModelScope.launch {
   _state.value = MainState.Loading
   _state.value = try {
    MainState.Users(repository.getUsers())
   } catch (e: Exception) {
    MainState.Error(e.localizedMessage)
   }
  }
 }

}