package com.example.jawlah.data.local.glance

import android.content.Context
import com.example.jawlah.data.local.realm.plan.entity.BudgetEntity
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

class GlanceRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val realm: Realm
){

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface GlanceRepositoryEntryPoint {
        fun widgetRepository(): GlanceRepository
    }

    companion object {
        fun get(applicationContext: Context): GlanceRepository {
            val widgetRepositoryEntryPoint: GlanceRepositoryEntryPoint =
                EntryPoints.get(
                    applicationContext,
                    GlanceRepositoryEntryPoint::class.java
                )

            return widgetRepositoryEntryPoint.widgetRepository()
        }
    }

    fun retrieveBudget(): BudgetEntity {
        return realm.query<BudgetEntity>().find().lastOrNull() ?: BudgetEntity()
    }
}