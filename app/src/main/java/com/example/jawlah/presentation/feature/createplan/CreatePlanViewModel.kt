package com.example.jawlah.presentation.feature.createplan

import androidx.lifecycle.viewModelScope
import com.example.jawlah.data.local.realm.plan.entity.PlanEntity
import com.example.jawlah.domain.myplans.MyPlansRepo
import com.example.jawlah.presentation.util.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreatePlanViewModel @Inject constructor(
    private val myPlansRepo: MyPlansRepo
) :
    BaseMviViewModel<CreatePlanContract.Event, CreatePlanContract.State, CreatePlanContract.Effect>() {

    override fun setInitialState(): CreatePlanContract.State = CreatePlanContract.State()

    override fun handleEvents(event: CreatePlanContract.Event) {
        when (event) {
            CreatePlanContract.Event.SavePlan -> {
                savePlan()
            }

            is CreatePlanContract.Event.AddDestination -> {
                setState {
                    copy(
                        destinations = destinations.apply { add(event.destination) },
                        destination = ""
                    )
                }
                checkContinueState()
            }

            is CreatePlanContract.Event.RemoveDestination -> {
                setState {
                    copy(destinations = destinations.apply { removeAt(event.index) })
                }
                checkContinueState()
            }

            is CreatePlanContract.Event.onDestinationChange -> {
                setState {
                    copy(destination = event.destination)
                }
            }

            is CreatePlanContract.Event.onEndDateChange -> {
                setState {
                    copy(endDate = event.endDate)
                }
                checkContinueState()
            }

            is CreatePlanContract.Event.onStartDateChange -> {
                setState {
                    copy(startDate = event.startDate)
                }
                checkContinueState()
            }

            is CreatePlanContract.Event.OnNameChange -> {
                setState {
                    copy(name = event.name)
                }
            }
        }
    }

    private fun savePlan() {
        viewModelScope.launch {
            val planId = UUID.randomUUID().toString()
            runCatching {
                myPlansRepo.insertPlan(
                    PlanEntity().apply {
                        id = planId
                        name = viewState.value.name
                        distenations = viewState.value.destinations
                        startDate = viewState.value.startDate
                        endDate = viewState.value.endDate
                    }
                )
                setEffect {
                    CreatePlanContract.Effect.Navigation.NavigateToPlanDetailScreen(planId, viewState.value.destinations)
                }
            }.onFailure {
                setEffect { CreatePlanContract.Effect.Error(it.message.toString()) }
            }
        }
    }

    private fun checkContinueState() {
        setState {
            copy(displayContinueButton = (destinations.size > 0) && startDate != 0L && endDate != 0L)
        }
    }
}