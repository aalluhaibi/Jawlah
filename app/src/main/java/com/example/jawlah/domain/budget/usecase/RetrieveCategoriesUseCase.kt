package com.example.jawlah.domain.budget.usecase

import com.example.jawlah.base.dispatcher.Dispatcher
import com.example.jawlah.base.usecase.FlowUseCaseWrapper
import com.example.jawlah.data.local.realm.plan.entity.CategoryEntity
import com.example.jawlah.domain.myplans.MyPlansRepo

class RetrieveCategoriesUseCase (
    dispatcher: Dispatcher,
    private val repo: MyPlansRepo
): FlowUseCaseWrapper<Unit?, List<CategoryEntity>>(dispatcher) {
    override suspend fun execute(parameters: Unit?): List<CategoryEntity> {
        return repo.retrieveCategory()
    }
}