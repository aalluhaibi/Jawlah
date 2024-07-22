package com.example.jawlah.presentation.feature.plandetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.jawlah.BuildConfig
import com.example.jawlah.UiState
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
    savedStateHandle: SavedStateHandle
) :
    BaseMviViewModel<PlanDetailsContract.Event, PlanDetailsContract.State, PlanDetailsContract.Effect>() {

    private val navArgsFromDestination: PlanDetailsScreenNavArgs =
        PlanDetailsScreenDestination.argsFrom(savedStateHandle)

    override fun setInitialState(): PlanDetailsContract.State = PlanDetailsContract.State()

    override fun handleEvents(event: PlanDetailsContract.Event) {
        when (event) {
            is PlanDetailsContract.Event.AddPlace -> {

            }

            PlanDetailsContract.Event.LoadSuggestions -> {
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
}