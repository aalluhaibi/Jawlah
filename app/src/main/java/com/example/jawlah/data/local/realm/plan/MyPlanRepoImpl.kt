package com.example.jawlah.data.local.realm.plan

import com.example.jawlah.data.local.realm.plan.entity.PlanEntity
import com.example.jawlah.domain.myplans.MyPlansRepo
import io.realm.kotlin.Realm
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
}