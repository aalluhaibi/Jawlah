package com.example.jawlah.presentation.feature.budget

import android.health.connect.datatypes.units.Percentage
import androidx.compose.runtime.mutableStateListOf
import com.example.jawlah.data.local.realm.plan.entity.CategoryEntity
import com.example.jawlah.data.local.realm.plan.entity.TransactionEntity
import com.example.jawlah.presentation.util.ViewEvent
import com.example.jawlah.presentation.util.ViewSideEffect
import com.example.jawlah.presentation.util.ViewState
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList

class BudgetContract {
    sealed class Event : ViewEvent {
        data object LoadBudget : Event()
        data class OnAddTransactionClick(val transactionEntity: TransactionEntity) : Event()
        data class OnEditTransactionClick(val id: String) : Event()
        data class OnDeleteTransactionClick(val id: String) : Event()
        data class OnSelectedFilterChanged(val index: Int) : Event()
        data object InsertTransaction : Event()
        data class InsertCategory(val category: String) : Event()
        data class AddIncome(val income: String) : Event()
        data class SelectTransactionType(val transactionType: TransactionType) : Event()
        data class TransactionAmount(val amount: String) : Event()
        data class OnDescriptionChanged(val description: String?) : Event()
        data class PaymentClassificationChanged(val text: String) : Event()
        data object PaymentCategory : Event()
        data object SetDateAndTime : Event()
    }

    data class State(
        val loading: Boolean = false,
        val currentTransaction: TransactionEntity? = null,
        val budgetId: String = "",
        val planId: String = "",
        val totalIncome: Double = 0.0,
        val totalExpense: Double = 0.0,
        val expensePercentage: Float = 0.0f,
        val incomePercentage: Float = 0.0f,
        val selectedFilterIndex: Int = 0,
        val categories: List<CategoryEntity> = listOf(),
        val transactions: MutableList<TransactionEntity> = mutableStateListOf(),
        val filteredTransactions: MutableMap<Long, List<TransactionEntity>> =mutableMapOf(),
        val transactionsMap: MutableMap<Long, List<TransactionEntity>> = mutableMapOf()
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        data class Error(
            val message: String,
            val action: (() -> Unit)? = null
        ) : Effect()


        sealed class Navigation : Effect() {
            data object Back : Navigation()
        }
    }
}

enum class TransactionType {
    EXPENSE,
    INCOME
}