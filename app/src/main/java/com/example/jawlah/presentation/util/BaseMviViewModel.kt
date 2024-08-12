package com.example.jawlah.presentation.util

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jawlah.BuildConfig
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface ViewEvent

interface ViewState

interface ViewSideEffect

const val SIDE_EFFECTS_KEY = "side-effects_key"

abstract class BaseMviViewModel<Event: ViewEvent, UiState: ViewState, Effect: ViewSideEffect> : ViewModel() {

    abstract fun setInitialState(): UiState
    abstract fun handleEvents(event: Event)

    private val initialState: UiState by lazy { setInitialState() }

    private val _viewState: MutableStateFlow<UiState> = MutableStateFlow(initialState)
    val viewState: StateFlow<UiState> = _viewState

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    private val _effect: Channel<Effect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        subscribeToEvents()
    }

    private fun subscribeToEvents() {
        viewModelScope.launch {
            _event.collect {
                handleEvents(it)
            }
        }
    }

    fun setEvent(event: Event) {
        if(BuildConfig.DEBUG) {
            Log.v("Event", "Event -> $event")
        }
        viewModelScope.launch { _event.emit(event) }
    }

    protected fun setState(reducer: UiState.() -> UiState) {
        _viewState.update {
            viewState.value.reducer()
        }
        if(BuildConfig.DEBUG) {
            Log.v("State", "New State -> ${viewState.value}")
        }
    }

    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        if(BuildConfig.DEBUG) {
            Log.v("Effect", "Effect -> $effectValue")
        }
        viewModelScope.launch { _effect.send(effectValue) }
    }
}