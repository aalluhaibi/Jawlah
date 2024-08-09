package com.example.jawlah.data.local.realm.plan

import com.example.jawlah.data.local.realm.plan.entity.BudgetEntity
import com.example.jawlah.data.local.realm.plan.entity.CategoryEntity
import com.example.jawlah.data.local.realm.plan.entity.PlaceEntity
import com.example.jawlah.data.local.realm.plan.entity.PlanEntity
import com.example.jawlah.data.local.realm.plan.entity.TransactionEntity
import com.example.jawlah.domain.myplans.MyPlansRepo
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.find
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class MyPlanRepoImpl(
    private val realm: Realm
) : MyPlansRepo {
    override suspend fun insertPlan(plan: PlanEntity) {
        val uuid = UUID.randomUUID().toString()
        realm.write {
            copyToRealm(
                PlanEntity().apply {
                    id = uuid
                    name = plan.name
                    distenations = plan.distenations
                    startDate = plan.startDate
                    endDate = plan.endDate
                }
            )
        }
    }

    override suspend fun insertPlace(place: PlaceEntity) {
        realm.write {
            val targetPlace =
                query<PlanEntity>("id == $0", place.planId).find().firstOrNull()

            if (targetPlace != null) {
                targetPlace.places.add(place)
            } else {
                // TODO: Handle the case where the budget isn't found
            }
        }
    }

    override suspend fun retrievePlans(): List<PlanEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun insertBudget(budget: BudgetEntity) {
        val uuid = if (budget.id.isEmpty())
            UUID.randomUUID().toString()
        else budget.id

        realm.write {
            copyToRealm(
                BudgetEntity().apply {
                    id = uuid
                    planId = budget.planId
                    totalExpense = budget.totalExpense
                    totalIncome = budget.totalIncome
                }
            )
        }
    }

    override suspend fun insertTransaction(transactionEntity: TransactionEntity) {
        realm.write {
            val targetBudget =
                query<BudgetEntity>("id == $0", transactionEntity.budgetId).find().firstOrNull()

            if (targetBudget != null) {
                targetBudget.transactionEntities.add(transactionEntity)
            } else {
                // TODO: Handle the case where the budget isn't found
            }
        }
    }

    override suspend fun insertTotalIncome(budgetId: String, totalIncome: Double) {
        realm.write {
            val targetBudget =
                query<BudgetEntity>("id == $0", budgetId).find().firstOrNull()

            if (targetBudget != null) {
                targetBudget.totalIncome = totalIncome
            } else {
                // TODO: Handle the case where the budget isn't found
            }
        }
    }

    override suspend fun retrieveBudget(planId: String): BudgetEntity {
        val budget = realm.query<BudgetEntity>(
            "planId == $0",
            planId
        ).find().firstOrNull() ?: BudgetEntity()

        return budget
    }

    override suspend fun retrieveCategory(): List<CategoryEntity> {
        return realm.query<CategoryEntity>().find()
    }

    override suspend fun insertCategory(categoryEntity: CategoryEntity) {
        realm.write {
            copyToRealm(categoryEntity)
        }
    }
}