package com.example.jawlah.presentation.feature.ask_gemi

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.jawlah.base.network.ApiResult
import com.example.jawlah.domain.ask_gemi.usecase.AskGemiGeneralQuestionUseCase
import com.example.jawlah.domain.ask_gemi.usecase.ClassifyLuggageUseCase
import com.example.jawlah.domain.ask_gemi.usecase.LandmarkLensUseCase
import com.example.jawlah.presentation.feature.destinations.AskGemiScreenDestination
import com.example.jawlah.presentation.feature.destinations.PlanDetailsScreenDestination
import com.example.jawlah.presentation.feature.plandetails.PlanDetailsScreenNavArgs
import com.example.jawlah.presentation.util.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AskGemiViewModel @Inject constructor(
    private val askGemiGeneralQuestionUseCase: AskGemiGeneralQuestionUseCase,
    private val classifyLuggageUseCase: ClassifyLuggageUseCase,
    private val landmarkLensUseCase: LandmarkLensUseCase,
    savedStateHandle: SavedStateHandle
) :
    BaseMviViewModel<AskGemiContract.Event, AskGemiContract.State, AskGemiContract.Effect>() {
    override fun setInitialState(): AskGemiContract.State = AskGemiContract.State()

    private val navArgsFromDestination: AskGemiScreenNavArgs =
        AskGemiScreenDestination.argsFrom(savedStateHandle)

    init {
        setState {
            copy(
                messages = listOf(
                    Message(
                        text = "Hey there, traveler! ✈️ Ready to turn your wanderlust into reality?  I'm here to help you discover awesome places, answer your burning travel questions, and build the perfect trip. Where should we start? 🗺️",
                        participant = Participant.GEMI
                    )
                ).toMutableList()
            )
        }
    }

    override fun handleEvents(event: AskGemiContract.Event) {
        when (event) {
            is AskGemiContract.Event.OnImageSelected -> TODO()
            is AskGemiContract.Event.OnTextMessageChanged -> {
                setState {
                    copy(textMessage = event.text)
                }
            }

            AskGemiContract.Event.ShowImagePicker -> TODO()
            is AskGemiContract.Event.SubmitGeneralMessage -> {
                event.message?.let {
                    submitGeneralMessage(it)
                    setState {
                        copy(
                            messages = (viewState.value.messages + Message(
                                text = event.message.text,
                                img = event.message.img,
                                participant = Participant.USER
                            )).toMutableList(),
                            textMessage = "",
                            message = null,
                        )
                    }
                }
            }

            is AskGemiContract.Event.SubmitLuggageClassificationMessage -> {
                submitLuggageClassificationMessage(event.message)
                setState {
                    copy(
                        messages = (viewState.value.messages + Message(
                            text = event.message.text,
                            img = event.message.img,
                            participant = Participant.USER
                        )).toMutableList(),
                        textMessage = "",
                        message = null,
                    )
                }
            }

            is AskGemiContract.Event.SubmitLandmarkLensMessage -> {
                submitLandmarkLensMessage(event.message)
                setState {
                    copy(
                        messages = (viewState.value.messages + Message(
                            text = event.message.text,
                            img = event.message.img,
                            participant = Participant.USER
                        )).toMutableList(),
                        textMessage = "",
                        message = null,
                    )
                }
            }
        }
    }

    private fun submitLuggageClassificationMessage(message: Message) {
        viewModelScope.launch {
            try {
                classifyLuggageUseCase(message).collect { response ->
                    when (response) {
                        is ApiResult.Error -> {}
                        ApiResult.Loading -> {}
                        ApiResult.Offline -> {}
                        is ApiResult.Success -> {
                            setState {
                                copy(
                                    messages = (viewState.value.messages + Message(
                                        text = response.value?.text ?: "",
                                        participant = Participant.GEMI
                                    )).toMutableList()
                                )
                            }
                        }
                    }
                }

            } catch (exception: Exception) {
                setEffect {
                    AskGemiContract.Effect.Error(exception.localizedMessage ?: "")
                }
                setState {
                    copy(loading = false, message = Message(participant = Participant.ERROR))
                }
            }
        }
    }

    private fun submitLandmarkLensMessage(message: Message) {
        viewModelScope.launch {
            try {
                landmarkLensUseCase(message).collect { response ->
                    when (response) {
                        is ApiResult.Error -> {}
                        ApiResult.Loading -> {}
                        ApiResult.Offline -> {}
                        is ApiResult.Success -> {
                            setState {
                                copy(
                                    messages = (viewState.value.messages + Message(
                                        text = response.value?.text ?: "",
                                        participant = Participant.GEMI
                                    )).toMutableList()
                                )
                            }
                        }
                    }
                }

            } catch (exception: Exception) {
                setEffect {
                    AskGemiContract.Effect.Error(exception.localizedMessage ?: "")
                }
                setState {
                    copy(loading = false, message = Message(participant = Participant.ERROR))
                }
            }
        }
    }

    private fun submitGeneralMessage(message: Message) {
        viewModelScope.launch {
            try {
                askGemiGeneralQuestionUseCase(message).collect { response ->
                    when (response) {
                        is ApiResult.Error -> {}
                        ApiResult.Loading -> {}
                        ApiResult.Offline -> {}
                        is ApiResult.Success -> {
                            setState {
                                copy(
                                    messages = (viewState.value.messages + Message(
                                        text = response.value?.text ?: "",
                                        participant = Participant.GEMI
                                    )).toMutableList()
                                )
                            }
                        }
                    }
                }

            } catch (exception: Exception) {
                setEffect {
                    AskGemiContract.Effect.Error(exception.localizedMessage ?: "")
                }
                setState {
                    copy(loading = false, message = Message(participant = Participant.ERROR))
                }
            }
        }
    }
}