package com.example.jawlah.presentation.feature.createplan

import com.example.jawlah.presentation.util.ViewEvent
import com.example.jawlah.presentation.util.ViewSideEffect
import com.example.jawlah.presentation.util.ViewState
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList

class CreatePlanContract {
    sealed class Event : ViewEvent {
        data object SavePlan : Event()
        data class AddDestination(val destination: String) : Event()
        data class OnNameChange(val name: String) : Event()
        data class RemoveDestination(val index: Int) : Event()
        data class onDestinationChange(val destination: String) : Event()
        data class onStartDateChange(val startDate: Long) : Event()
        data class onEndDateChange(val endDate: Long) : Event()
    }

    data class State(
        val loading: Boolean = false,
        val displayContinueButton: Boolean = false,
        val id: String = "",
        val destination: String = "",
        val destinations: RealmList<String> = realmListOf(),
        val name: String = "",
        val startDate: Long = 0L,
        val endDate: Long = 0L
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class Error(
            val message: String,
            val action: (() -> Unit)? = null
        ) : Effect()


        sealed class Navigation : Effect() {
            data class NavigateToPlanDetailScreen(val id: String) : Navigation()
            data object Back : Navigation()
        }
    }
}