package com.example.jawlah.domain.budget.usecase

import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.base.usecase.FlowUseCaseWrapper
import com.example.jawlah.data.local.realm.plan.entity.PlaceEntity
import com.example.jawlah.domain.myplans.MyPlansRepo

class AddPlaceUseCase(
    dispatcher: Dispatcher,
    private val repo: MyPlansRepo
) : FlowUseCaseWrapper<AddPlaceRequest, Unit>(dispatcher) {
    override suspend fun execute(parameters: AddPlaceRequest) {
        repo.insertPlace(parameters.place)
    }
}

data class AddPlaceRequest(
    val place: PlaceEntity
)