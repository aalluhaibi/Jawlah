package com.example.jawlah.domain.budget.usecase

import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.base.usecase.FlowUseCaseWrapper
import com.example.jawlah.data.local.realm.plan.entity.TransactionEntity
import com.example.jawlah.domain.myplans.MyPlansRepo

class AddTransactionUseCase(
    dispatcher: Dispatcher,
    private val repo: MyPlansRepo
) : FlowUseCaseWrapper<AddTransactionRequest, Unit>(dispatcher) {
    override suspend fun execute(parameters: AddTransactionRequest) {
        repo.insertTransaction(parameters.transaction)
    }
}

data class AddTransactionRequest(
    val transaction: TransactionEntity
)