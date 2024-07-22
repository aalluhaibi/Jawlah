package com.example.jawlah.domain.myplans

import com.example.jawlah.data.local.realm.plan.entity.BudgetEntity
import com.example.jawlah.data.local.realm.plan.entity.PlanEntity
import com.example.jawlah.data.local.realm.plan.entity.TransactionEntity
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow

interface MyPlansRepo {
    suspend fun insertPlan(plan: PlanEntity)
    suspend fun retrievePlans(): List<PlanEntity>
    suspend fun insertBudget(budget: BudgetEntity)
    suspend fun insertTransaction(transactionEntity: TransactionEntity)
    suspend fun retrieveBudget(planId: String): BudgetEntity
}