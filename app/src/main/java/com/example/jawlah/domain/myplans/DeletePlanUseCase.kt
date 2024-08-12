package com.example.jawlah.domain.myplans

import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.base.usecase.FlowUseCaseWrapper
import com.example.jawlah.data.local.realm.plan.entity.PlanEntity

class DeletePlanUseCase(
    dispatcher: Dispatcher,
    private val repo: MyPlansRepo
) : FlowUseCaseWrapper<DeletePlanRequest, Unit>(dispatcher) {
    override suspend fun execute(parameters: DeletePlanRequest) {
        repo.deletePlan(parameters.plan)
    }
}

data class DeletePlanRequest(
    val plan: PlanEntity
)