package com.example.jawlah.presentation.feature.myplans

import androidx.compose.runtime.mutableStateListOf
import com.example.jawlah.data.local.realm.plan.entity.PlanEntity
import com.example.jawlah.presentation.util.ViewEvent
import com.example.jawlah.presentation.util.ViewSideEffect
import com.example.jawlah.presentation.util.ViewState

class MyPlansContract {
    sealed class Event: ViewEvent {
        data object CreatePlan: Event()
    }

    data class State(
        val loading: Boolean = false,
        val myPlans: MutableList<PlanEntity> = mutableStateListOf()
    ): ViewState

    sealed class Effect: ViewSideEffect {
        data class Error(
            val message: String,
            val action: (() -> Unit)? = null
        ): Effect()


        sealed class Navigation: Effect() {
            data object NavigateToCreateNewPlan: Navigation()
            data class NavigateToPlanDetails(val id: String, val listOfDestinations: String): Navigation()
            data object AskGemi: Navigation()
        }
    }
}