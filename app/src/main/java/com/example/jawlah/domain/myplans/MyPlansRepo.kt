package com.example.jawlah.domain.myplans

import com.example.jawlah.data.local.realm.plan.entity.PlanEntity

interface MyPlansRepo {
    suspend fun insertPlan(plan: PlanEntity)
}