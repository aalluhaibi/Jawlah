package com.example.jawlah.domain.budget.usecase

import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.base.usecase.FlowUseCaseWrapper
import com.example.jawlah.data.local.realm.plan.entity.BudgetEntity
import com.example.jawlah.domain.myplans.MyPlansRepo

class RetrieveBudgetUseCase(
    dispatcher: Dispatcher,
    private val repo: MyPlansRepo
): FlowUseCaseWrapper<Request, BudgetEntity>(dispatcher) {
    override suspend fun execute(parameters: Request): BudgetEntity {
        return repo.retrieveBudget(parameters.planId)
    }
}

data class Request(
    val planId: String
)