package com.example.jawlah.domain.budget.usecase

import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.base.usecase.FlowUseCaseWrapper
import com.example.jawlah.domain.myplans.MyPlansRepo

class AddTotalIncomeUseCase(
    dispatcher: Dispatcher,
    private val repo: MyPlansRepo
) : FlowUseCaseWrapper<AddTotalIncomeRequest, Unit>(dispatcher) {
    override suspend fun execute(parameters: AddTotalIncomeRequest) {
        repo.insertTotalIncome(parameters.budgetId, parameters.totalIncome)
    }

}

data class AddTotalIncomeRequest(
    val budgetId: String,
    val totalIncome: Double
)