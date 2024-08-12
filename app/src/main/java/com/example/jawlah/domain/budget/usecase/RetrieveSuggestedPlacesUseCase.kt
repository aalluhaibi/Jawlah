package com.example.jawlah.domain.budget.usecase

import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.base.usecase.FlowUseCaseWrapper
import com.example.jawlah.domain.myplans.MyPlansRepo

class RetrieveSuggestedPlacesUseCase(
    dispatcher: Dispatcher,
    private val repo: MyPlansRepo
) : FlowUseCaseWrapper<RetrieveSuggestedPlacesRequest, List<String>>(dispatcher) {
    override suspend fun execute(parameters: RetrieveSuggestedPlacesRequest): List<String> {
        return repo.retrieveAIPlaceRecommendations(parameters.destination)
    }
}

data class RetrieveSuggestedPlacesRequest(
    val destination: String
)