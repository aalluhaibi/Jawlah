package com.example.jawlah.presentation.feature.plandetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.jawlah.base.network.ApiResult
import com.example.jawlah.data.local.realm.plan.entity.PlaceEntity
import com.example.jawlah.data.local.realm.plan.entity.PlaceType
import com.example.jawlah.domain.budget.usecase.AddPlaceRequest
import com.example.jawlah.domain.budget.usecase.AddPlaceUseCase
import com.example.jawlah.domain.budget.usecase.RetrievePlacesRequest
import com.example.jawlah.domain.budget.usecase.RetrievePlacesUseCase
import com.example.jawlah.domain.budget.usecase.RetrieveSuggestedPlacesRequest
import com.example.jawlah.domain.budget.usecase.RetrieveSuggestedPlacesUseCase
import com.example.jawlah.presentation.feature.destinations.PlanDetailsScreenDestination
import com.example.jawlah.presentation.util.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanDetailsViewModel @Inject constructor(
    private val addPlaceUseCase: AddPlaceUseCase,
    private val retrievePlacesUseCase: RetrievePlacesUseCase,
    private val retrieveSuggestedPlacesUseCase: RetrieveSuggestedPlacesUseCase,
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
                    type = event.place.type
                    locationUrl = event.place.locationUrl
                    description = event.place.description
                })
            }

            PlanDetailsContract.Event.LoadSuggestions -> {
                retrievePlaces()
                retrieveSuggestedPlaces(
                    navArgsFromDestination.listOfDestinations
                )
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

    private fun retrieveSuggestedPlaces(
        destinations: String
    ) {
        setState {
            copy(loading = true, aiLoading = true)
        }

        viewModelScope.launch(Dispatchers.IO) {
            retrieveSuggestedPlacesUseCase.invoke(RetrieveSuggestedPlacesRequest(destinations))
                .collect { result ->
                    when (result) {
                        is ApiResult.Error -> {
                            setEffect { PlanDetailsContract.Effect.Error(result.message) }
                        }

                        ApiResult.Loading -> {
                            setState {
                                copy(
                                    loading = false,
                                    aiLoading = true
                                )
                            }
                        }

                        ApiResult.Offline -> {
                            setState {
                                copy(
                                    loading = false
                                )
                            }

                        }

                        is ApiResult.Success -> {
                            setState {
                                copy(
                                    loading = false,
                                    aiLoading = false,
                                    suggestedPlaces = result.value.toRealmList()
                                )
                            }
                        }
                    }
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
                            copy(
                                loading = false,
                                locations = it.value.toMutableList(),
                                places = it.value.filter { it.type == PlaceType.Place }
                                    .toMutableList(),
                                activities = it.value.filter { it.type == PlaceType.Activity }
                                    .toMutableList(),
                                lodging = it.value.filter { it.type == PlaceType.Lodging }
                                    .toMutableList(),
                                others = it.value.filter { it.type == PlaceType.Other }
                                    .toMutableList()
                            )
                        }
                    }
                }
            }
        }
    }
}