package com.example.jawlah.presentation.feature.myplans

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.jawlah.BuildConfig
import com.example.jawlah.UiState
import com.example.jawlah.data.local.realm.plan.entity.PlanEntity
import com.example.jawlah.presentation.util.BaseMviViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPlansViewModel @Inject constructor(
    private val realm: Realm,
) :
    BaseMviViewModel<MyPlansContract.Event, MyPlansContract.State, MyPlansContract.Effect>() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow()

    init {
        retrieveMyPlans()
    }

    private fun retrieveMyPlans() {
        viewModelScope.launch {
            runCatching {
                val violationsFlow = realm.query<PlanEntity>().asFlow()
                violationsFlow.collect { result ->
                    setState { copy(myPlans = result.list.toMutableList()) }
                }
            }.onFailure {
                setEffect { MyPlansContract.Effect.Error(it.message.toString()) }
            }
        }
    }

    override fun setInitialState(): MyPlansContract.State = MyPlansContract.State()

    override fun handleEvents(event: MyPlansContract.Event) {
        when (event) {
            MyPlansContract.Event.CreatePlan -> TODO()
        }
    }

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash", //gemini-pro-vision
        apiKey = BuildConfig.apiKey
    )

    fun sendPrompt() {
        _uiState.value = UiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        //image(bitmap)
                        text("Recommend me places to visit in my trip to Milan split each place by new line")
                    }
                )
                response.text?.let { outputContent ->
                    _uiState.value = UiState.Success(outputContent)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}