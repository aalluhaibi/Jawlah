package com.example.jawlah.domain.budget.usecase

import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.base.usecase.FlowUseCaseWrapper
import com.example.jawlah.data.local.realm.plan.entity.BudgetEntity
import com.example.jawlah.domain.myplans.MyPlansRepo

class CreateBudgetUseCase(
    dispatcher: Dispatcher,
    private val repo: MyPlansRepo
): FlowUseCaseWrapper<CreateBudgetRequest, Unit>(dispatcher) {
    override suspend fun execute(parameters: CreateBudgetRequest): Unit {
        return repo.insertBudget(parameters.budget)
    }
}

data class CreateBudgetRequest(
    val budget: BudgetEntity
)