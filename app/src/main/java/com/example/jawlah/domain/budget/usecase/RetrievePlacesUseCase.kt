package com.example.jawlah.domain.budget.usecase

import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.base.usecase.FlowUseCaseWrapper
import com.example.jawlah.data.local.realm.plan.entity.PlaceEntity
import com.example.jawlah.domain.myplans.MyPlansRepo

class RetrievePlacesUseCase(
    dispatcher: Dispatcher,
    private val repo: MyPlansRepo
) : FlowUseCaseWrapper<RetrievePlacesRequest, List<PlaceEntity>>(dispatcher) {
    override suspend fun execute(parameters: RetrievePlacesRequest): List<PlaceEntity> {
        return repo.retrievePlaces(parameters.planId)
    }
}

data class RetrievePlacesRequest(
    val planId: String
)