package com.example.jawlah.data.local.realm.plan.entity

import com.example.jawlah.presentation.feature.budget.TransactionType
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class BudgetEntity: RealmObject {
    @PrimaryKey
    var id: String = ""
    var planId: String = ""
    var totalIncome: Double = 0.0
    var totalExpense: Double = 0.0
    var transactionEntities: RealmList<TransactionEntity> = realmListOf()
}

class TransactionEntity: EmbeddedRealmObject {
    var budgetId: String = ""
    var amount: String = ""
    var category: String = ""
    private var type: String = TransactionType.INCOME.name
    var transactionType: TransactionType get() = TransactionType.valueOf(type)
    set(value) {
        type = value.name
    }
    var description: String = ""
    var date: Long = 0L
    var time: Long? = null
}