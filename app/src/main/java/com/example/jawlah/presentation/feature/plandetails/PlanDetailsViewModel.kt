package com.example.jawlah.presentation.feature.plandetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.jawlah.BuildConfig
import com.example.jawlah.UiState
import com.example.jawlah.base.network.ApiResult
import com.example.jawlah.data.local.realm.plan.entity.PlaceEntity
import com.example.jawlah.data.local.realm.plan.entity.PlaceType
import com.example.jawlah.domain.budget.usecase.AddPlaceRequest
import com.example.jawlah.domain.budget.usecase.AddPlaceUseCase
import com.example.jawlah.domain.budget.usecase.RetrievePlacesRequest
import com.example.jawlah.domain.budget.usecase.RetrievePlacesUseCase
import com.example.jawlah.presentation.feature.destinations.PlanDetailsScreenDestination
import com.example.jawlah.presentation.util.BaseMviViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanDetailsViewModel @Inject constructor(
    private val addPlaceUseCase: AddPlaceUseCase,
    private val retrievePlacesUseCase: RetrievePlacesUseCase,
    savedStateHandle: SavedStateHandle
) :
    BaseMviViewModel<PlanDetailsContract.Event, PlanDetailsContract.State, PlanDetailsContract.Effect>() {

    private val navArgsFromDestination: PlanDetailsScreenNavArgs =
        PlanDetailsScreenDestination.argsFrom(savedStateHandle)

    override fun setInitialState(): PlanDetailsContract.State = PlanDetailsContract.State()

    override fun handleEvents(event: PlanDetailsContract.Event) {
        when (event) {
            is PlanDetailsContract.Event.AddPlace -> {
                addPlace(PlaceEntity().apply {
                    name = event.place.name
                    planId = navArgsFromDestination.id
                    type = PlaceType.Place
                    locationUrl = event.place.locationUrl
                    description = event.place.description
                })
            }

            PlanDetailsContract.Event.LoadSuggestions -> {
                retrievePlaces()
                retrieveSuggestedPlaces("Recommend me places names (Only names I need) to visit in my trip to Milan split each place by new line. I looking for a response as an array of names separated by new line")
            }

            PlanDetailsContract.Event.Init -> {
                setState {
                    copy(
                        planId = navArgsFromDestination.id
                    )
                }
            }
        }
    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash", //gemini-pro-vision
        apiKey = BuildConfig.apiKey,
        generationConfig = generationConfig {
            temperature = 0.7f
        },
        systemInstruction = content {
            text("You are a capable travel assistant named Gemi.")
        }
    )

    private fun retrieveSuggestedPlaces(
        prompt: String
    ) {
        setState {
            copy(loading = true)
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(prompt)
                    }
                )
                response.text?.let { outputContent ->
                    val suggestedPlaces = outputContent.split("\n").map { it.trim() }
                    setState {
                        copy(loading = false, suggestedPlaces = suggestedPlaces.toRealmList())
                    }
                }
            } catch (e: Exception) {
                setEffect { PlanDetailsContract.Effect.Error(e.localizedMessage ?: "") }
            }
        }
    }

    private fun addPlace(place: PlaceEntity) {
        viewModelScope.launch {
            addPlaceUseCase(
                AddPlaceRequest(place)
            ).collect {
                when (it) {
                    is ApiResult.Error -> {
                        setState {
                            copy(loading = false)
                        }
                    }

                    ApiResult.Loading -> {
                        setState {
                            copy(loading = true)
                        }
                    }

                    ApiResult.Offline -> {
                        setState {
                            copy(loading = false)
                        }
                    }

                    is ApiResult.Success -> {
                        setState {
                            copy(loading = false)
                        }
                        retrievePlaces()
                    }
                }
            }
        }
    }

    private fun retrievePlaces() {
        viewModelScope.launch {
            retrievePlacesUseCase(
                RetrievePlacesRequest(planId = navArgsFromDestination.id)
            ).collect {
                when (it) {
                    is ApiResult.Error -> {
                        setState {
                            copy(loading = false)
                        }
                    }

                    ApiResult.Loading -> {
                        setState {
                            copy(loading = true)
                        }
                    }

                    ApiResult.Offline -> {
                        setState {
                            copy(loading = false)
                        }
                    }

                    is ApiResult.Success -> {
                        setState {
                            copy(loading = false, places = it.value.toMutableList())
                        }
                    }
                }
            }
        }
    }
}