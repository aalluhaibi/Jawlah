package com.example.jawlah.domain.budget.usecase

import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.base.usecase.FlowUseCaseWrapper
import com.example.jawlah.data.local.realm.plan.entity.TransactionEntity
import com.example.jawlah.domain.myplans.MyPlansRepo

class DeleteTransactionUseCase(
    dispatcher: Dispatcher,
    private val repo: MyPlansRepo
) : FlowUseCaseWrapper<DeleteTransactionRequest, Unit>(dispatcher) {
    override suspend fun execute(parameters: DeleteTransactionRequest) {
        repo.deleteTransaction(parameters.transaction)
    }
}

data class DeleteTransactionRequest(
    val transaction: TransactionEntity
)