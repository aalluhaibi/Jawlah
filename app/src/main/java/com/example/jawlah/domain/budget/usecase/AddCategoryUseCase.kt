package com.example.jawlah.domain.budget.usecase

import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.base.usecase.FlowUseCaseWrapper
import com.example.jawlah.data.local.realm.plan.entity.CategoryEntity
import com.example.jawlah.domain.myplans.MyPlansRepo

class AddCategoryUseCase(
    dispatcher: Dispatcher,
    private val repo: MyPlansRepo
): FlowUseCaseWrapper<CategoryEntity, Unit>(dispatcher) {
    override suspend fun execute(parameters: CategoryEntity) {
        repo.insertCategory(parameters)
    }
}