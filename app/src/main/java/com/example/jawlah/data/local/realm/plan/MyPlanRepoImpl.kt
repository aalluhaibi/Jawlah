package com.example.jawlah.data.local.realm.plan

import com.example.jawlah.data.local.glance.GlanceRepository
import com.example.jawlah.data.local.realm.plan.entity.BudgetEntity
import com.example.jawlah.data.local.realm.plan.entity.CategoryEntity
import com.example.jawlah.data.local.realm.plan.entity.PlaceEntity
import com.example.jawlah.data.local.realm.plan.entity.PlanEntity
import com.example.jawlah.data.local.realm.plan.entity.TransactionEntity
import com.example.jawlah.domain.myplans.MyPlansRepo
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import java.util.UUID

class MyPlanRepoImpl(
    private val realm: Realm,
    private val generativeModel: GenerativeModel,
    private val glanceRepo: GlanceRepository
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

    override suspend fun retrievePlaces(planId: String): List<PlaceEntity> {
        val places = realm.query<PlaceEntity>(
            "planId == $0",
            planId
        ).find().toList()

        return places
    }

    override suspend fun retrieveAIPlaceRecommendations(destinations: String): List<String> {
        val prompt = "Recommend me places names (10 names for each destination) to visit in my trip to $destinations split each place by new line. only list the names of places even if it was more than a destination list them all together. look at the following example to visit Paris & London: " +
                "prompt: Recommend me places names (10 names for each destination) to visit in my trip to Paris & London split each place by new line. only list the names of places even if it was more than a destination list them all together. " + " response: Eiffel Tower\\nLouvre Museum\\nArc de Triomphe\\nNotre Dame Cathedral\\nMusée d'Orsay\\nSacré-Cœur Basilica\\nMontmartre\\nThe Palace of Versailles\\nPère Lachaise Cemetery\\nLatin Quarter\\nMusée Picasso\\nCentre Pompidou\\nÎle de la Cité\\nMusée Rodin\\nChamps-Élysées\\nTuileries Garden\\nCatacombs of Paris\\nMusée du Quai Branly - Jacques Chirac\\nSaint-Germain-des-Prés\\nPlace de la Concorde\\nBuckingham Palace\\nTower of London\\nThe British Museum\\nHouses of Parliament\\nLondon Eye\\nSt. Paul's Cathedral\\nTower Bridge\\nNational Gallery\\nWestminster Abbey\\nHyde Park\\nKensington Palace\\nThe Shard\\nShakespeare's Globe Theatre\\nThe National Portrait Gallery\\nThe Tate Modern\\nThe Victoria and Albert Museum\\nThe Churchill War Rooms\\nThe Royal Albert Hall\\nHampstead Heath\\nKew Gardens"

        try {
            val response = generativeModel.generateContent(
                content {
                    text(prompt)
                }
            )

            response.text?.let { outputContent ->
                return outputContent.split("\n").map { it.trim() }
            }
            return emptyList()
        } catch (e: Exception) {
            // TODO: Handle the exception
            return emptyList()
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
            glanceRepo.retrieveBudget()
        }
    }

    override suspend fun insertTransaction(transactionEntity: TransactionEntity) {
        realm.write {
            val targetBudget =
                query<BudgetEntity>("id == $0", transactionEntity.budgetId).find().firstOrNull()

            if (targetBudget != null) {
                targetBudget.transactionEntities.add(transactionEntity)
                glanceRepo.retrieveBudget()
            } else {
                // TODO: Handle the case where the budget isn't found
            }
        }
    }

    override suspend fun deleteTransaction(transactionEntity: TransactionEntity) {
        realm.write {
            val targetBudget =
                query<BudgetEntity>("id == $0", transactionEntity.budgetId).find().firstOrNull()

            if (targetBudget != null) {
                val transactionToDelete =
                    targetBudget.transactionEntities.find { it == transactionEntity }

                if (transactionToDelete != null) {
                    targetBudget.transactionEntities.remove(transactionToDelete)
                    glanceRepo.retrieveBudget()
                } else {
                    // Handle the case where the transaction isn't found within the budget
                }
            } else {
                // Handle the case where the budget isn't found
            }
        }
    }

    override suspend fun insertTotalIncome(budgetId: String, totalIncome: Double) {
        realm.write {
            val targetBudget =
                query<BudgetEntity>("id == $0", budgetId).find().firstOrNull()

            if (targetBudget != null) {
                targetBudget.totalIncome = totalIncome
                glanceRepo.retrieveBudget()
            } else {
                // Handle the case where the budget isn't found
            }
        }
    }

    override suspend fun retrieveBudget(planId: String): BudgetEntity {
        val budget = realm.query<BudgetEntity>(
            "planId == $0",
            planId
        ).find().firstOrNull() ?: BudgetEntity()
        glanceRepo.retrieveBudget()
        return budget
    }

    override suspend fun retrieveCategory(): List<CategoryEntity> {
        glanceRepo.retrieveBudget()
        return realm.query<CategoryEntity>().find()
    }

    override suspend fun insertCategory(categoryEntity: CategoryEntity) {
        realm.write {
            copyToRealm(categoryEntity)
            glanceRepo.retrieveBudget()
        }
    }
}