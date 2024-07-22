package com.example.jawlah.presentation.feature.plandetails

import com.example.jawlah.presentation.util.ViewEvent
import com.example.jawlah.presentation.util.ViewSideEffect
import com.example.jawlah.presentation.util.ViewState
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList

class PlanDetailsContract {
    sealed class Event : ViewEvent {
        data class AddPlace(val place: String) : Event()
        data object Init : Event()
        data object LoadSuggestions : Event()
    }

    data class State(
        val loading: Boolean = false,
        val planId: String = "",
        val destinations: RealmList<String> = realmListOf(),
        val places: RealmList<String> = realmListOf(),
        val suggestedPlaces: RealmList<String> = realmListOf(),
        val suggestedHotels: RealmList<String> = realmListOf(),
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
            data object Back : Navigation()
            data object AITest : Navigation()
            data class Budget(val planId: String) : Navigation()
        }
    }
}