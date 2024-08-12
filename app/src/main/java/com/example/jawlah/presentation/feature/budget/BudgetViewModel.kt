package com.example.jawlah.presentation.feature.budget

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.jawlah.base.network.ApiResult
import com.example.jawlah.data.local.realm.plan.entity.BudgetEntity
import com.example.jawlah.data.local.realm.plan.entity.CategoryEntity
import com.example.jawlah.data.local.realm.plan.entity.TransactionEntity
import com.example.jawlah.domain.budget.usecase.AddCategoryUseCase
import com.example.jawlah.domain.budget.usecase.AddTotalIncomeRequest
import com.example.jawlah.domain.budget.usecase.AddTotalIncomeUseCase
import com.example.jawlah.domain.budget.usecase.AddTransactionRequest
import com.example.jawlah.domain.budget.usecase.AddTransactionUseCase
import com.example.jawlah.domain.budget.usecase.CreateBudgetRequest
import com.example.jawlah.domain.budget.usecase.CreateBudgetUseCase
import com.example.jawlah.domain.budget.usecase.DeleteTransactionRequest
import com.example.jawlah.domain.budget.usecase.DeleteTransactionUseCase
import com.example.jawlah.domain.budget.usecase.Request
import com.example.jawlah.domain.budget.usecase.RetrieveBudgetUseCase
import com.example.jawlah.domain.budget.usecase.RetrieveCategoriesUseCase
import com.example.jawlah.presentation.feature.destinations.BudgetScreenDestination
import com.example.jawlah.presentation.util.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val retrieveBudgetUseCase: RetrieveBudgetUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val createBudgetUseCase: CreateBudgetUseCase,
    private val addTotalIncomeUseCase: AddTotalIncomeUseCase,
    private val retrieveCategoriesUseCase: RetrieveCategoriesUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    savedStateHandle: SavedStateHandle
) : BaseMviViewModel<BudgetContract.Event, BudgetContract.State, BudgetContract.Effect>() {
    override fun setInitialState(): BudgetContract.State = BudgetContract.State()

    private val navArgsFromDestination: BudgetScreenNavArgs =
        BudgetScreenDestination.argsFrom(savedStateHandle)

    override fun handleEvents(event: BudgetContract.Event) {
        when (event) {
            is BudgetContract.Event.OnAddTransactionClick -> {
                addTransaction(TransactionEntity().apply {
                    amount = event.transactionEntity.amount
                    description = event.transactionEntity.description
                    category = event.transactionEntity.category
                    budgetId = viewState.value.budgetId
                    date = event.transactionEntity.date
                    time = event.transactionEntity.time
                    transactionType = event.transactionEntity.transactionType
                })
            }

            is BudgetContract.Event.OnDeleteTransactionClick -> {
                deleteTransaction(event.transactionEntity)
            }

            is BudgetContract.Event.OnDescriptionChanged -> TODO()
            is BudgetContract.Event.OnEditTransactionClick -> TODO()
            BudgetContract.Event.PaymentCategory -> TODO()
            is BudgetContract.Event.PaymentClassificationChanged -> TODO()
            is BudgetContract.Event.SelectTransactionType -> TODO()
            BudgetContract.Event.SetDateAndTime -> TODO()
            is BudgetContract.Event.TransactionAmount -> TODO()
            BudgetContract.Event.LoadBudget -> {
                setState {
                    copy(planId = navArgsFromDestination.id)
                }
                retrieveBudget()
                retrieveCategories()
            }

            is BudgetContract.Event.OnSelectedFilterChanged -> {
                setState {
                    copy(
                        selectedFilterIndex = event.index,
                        filteredTransactions = filterTransactions(
                            viewState.value.transactionsMap,
                            event.index
                        )
                    )
                }
            }

            is BudgetContract.Event.InsertCategory -> {
                insertCategory(event.category)
            }

            is BudgetContract.Event.AddIncome -> {
                addToBudget(event.income.toDouble())
            }
        }
    }

    private fun addTransaction(transactionEntity: TransactionEntity) {
        viewModelScope.launch {
            addTransactionUseCase(
                AddTransactionRequest(
                    transaction = TransactionEntity().apply {
                        amount = transactionEntity.amount
                        description = transactionEntity.description
                        category = transactionEntity.category
                        budgetId = viewState.value.budgetId
                        date = transactionEntity.date
                        time = transactionEntity.time
                    }
                )
            ).collect { result ->
                when (result) {
                    is ApiResult.Error -> {

                    }

                    ApiResult.Loading -> {

                    }

                    ApiResult.Offline -> {}
                    is ApiResult.Success -> {
                        setState {
                            copy(
                                loading = false,
                                totalExpense = calculateTotalExpense(viewState.value.transactions),
                                expensePercentage = (calculateTotalExpense(viewState.value.transactions) / viewState.value.totalIncome).toFloat(),
                                incomePercentage = (1 - (calculateTotalExpense(viewState.value.transactions) / viewState.value.totalIncome).toFloat())
                            )
                        }
                        retrieveBudget()
                    }
                }
            }
        }
    }

    private fun deleteTransaction(transactionEntity: TransactionEntity) {
        viewModelScope.launch {
            deleteTransactionUseCase(DeleteTransactionRequest(transaction = transactionEntity)).collect { result ->
                when (result) {
                    is ApiResult.Error -> {
                    }

                    ApiResult.Loading -> {

                    }

                    ApiResult.Offline -> {

                    }

                    is ApiResult.Success -> {
                        retrieveBudget()
                    }
                }
            }
        }
    }

    private fun retrieveBudget() {
        viewModelScope.launch {
            retrieveBudgetUseCase(Request(navArgsFromDestination.id)).collect { result ->
                when (result) {
                    is ApiResult.Error -> {
                        setState {
                            copy(loading = false)
                        }
                        setEffect {
                            BudgetContract.Effect.Error(result.message)
                        }
                    }

                    ApiResult.Loading -> {
                        setState {
                            copy(loading = true)
                        }
                    }

                    is ApiResult.Success -> {
                        if (result.value.id.isEmpty()) {
                            createBudget()
                        }
                        val expense = calculateTotalExpense(viewState.value.transactions)
                        setState {
                            copy(
                                loading = false,
                                budgetId = result.value.id,
                                totalIncome = result.value.totalIncome,
                                totalExpense = calculateTotalExpense(result.value.transactionEntities),
                                expensePercentage = (calculateTotalExpense(result.value.transactionEntities) / result.value.totalIncome).toFloat(),
                                incomePercentage = (1 - (calculateTotalExpense(result.value.transactionEntities) / result.value.totalIncome).toFloat()),
                                transactions = result.value.transactionEntities.toMutableList(),
                                filteredTransactions = result.value.transactionEntities.groupBy { it.date }
                                    .toMutableMap(),
                                transactionsMap = result.value.transactionEntities.groupBy { it.date }
                                    .toMutableMap()
                            )
                        }
                    }

                    ApiResult.Offline -> {
                        setState {
                            copy(loading = false)
                        }
                    }
                }
            }
        }
    }

    private fun createBudget() {
        viewModelScope.launch {
            val request = CreateBudgetRequest(budget = BudgetEntity().apply {
                planId = navArgsFromDestination.id
                totalIncome = viewState.value.totalIncome
                totalExpense = viewState.value.totalExpense
            })
            createBudgetUseCase(request).collect { result ->
                when (result) {
                    is ApiResult.Error -> {
                        setEffect {
                            BudgetContract.Effect.Error(result.message)
                        }
                    }

                    ApiResult.Loading -> {
                        setState {
                            copy(loading = true)
                        }
                    }

                    ApiResult.Offline -> {
                        setState {
                            copy(loading = false)
                        }
                    }

                    is ApiResult.Success -> {
                        setState {
                            copy(
                                loading = false,
                                expensePercentage = (calculateTotalExpense(viewState.value.transactions) / viewState.value.totalIncome).toFloat(),
                                incomePercentage = (1 - (calculateTotalExpense(viewState.value.transactions) / viewState.value.totalIncome).toFloat())
                            )
                        }
                        retrieveBudget()
                    }
                }
            }
        }
    }

    private fun addToBudget(income: Double) {
        viewModelScope.launch {
            val request = AddTotalIncomeRequest(
                budgetId = viewState.value.budgetId,
                totalIncome = income
            )

            addTotalIncomeUseCase(request).collect { result ->
                when (result) {
                    is ApiResult.Error -> {
                        setEffect {
                            BudgetContract.Effect.Error(result.message)
                        }
                    }

                    ApiResult.Loading -> {
                        setState {
                            copy(loading = true)
                        }
                    }

                    ApiResult.Offline -> {
                        setState {
                            copy(loading = false)
                        }
                    }

                    is ApiResult.Success -> {
                        setState {
                            copy(
                                loading = false,
                                expensePercentage = (calculateTotalExpense(viewState.value.transactions) / viewState.value.totalIncome).toFloat(),
                                incomePercentage = (1 - (calculateTotalExpense(viewState.value.transactions) / viewState.value.totalIncome)).toFloat()
                            )
                        }
                        retrieveBudget()
                    }
                }
            }
        }
    }

    private fun retrieveCategories() {
        viewModelScope.launch {
            retrieveCategoriesUseCase(null).collect { result ->
                when (result) {
                    is ApiResult.Error -> {}
                    ApiResult.Loading -> {

                    }

                    ApiResult.Offline -> {}
                    is ApiResult.Success -> {
                        setState {
                            copy(categories = result.value.toMutableList())
                        }
                    }
                }
            }
        }
    }

    private fun insertCategory(category: String) {
        viewModelScope.launch {
            val id = java.util.UUID.randomUUID().toString()
            addCategoryUseCase(CategoryEntity().apply {
                this.id = id
                this.name = category
            }).collect { result ->
                when (result) {
                    is ApiResult.Error -> {}
                    ApiResult.Loading -> {

                    }

                    ApiResult.Offline -> {}
                    is ApiResult.Success -> {
                        setState {
                            copy(loading = false)
                        }
                        retrieveCategories()
                    }
                }
            }
        }
    }

    private fun filterTransactions(
        transactionsMap: MutableMap<Long, List<TransactionEntity>>,
        filter: Int
    ): MutableMap<Long, List<TransactionEntity>> {
        val currentTime = System.currentTimeMillis()

        return when (filter) {
            0 -> transactionsMap // No filtering
            1 -> {
                val oneWeekAgo = currentTime - 7 * 24 * 60 * 60 * 1000  // 7 days in milliseconds
                transactionsMap.filterKeys { it >= oneWeekAgo }.toMutableMap()
            }

            2 -> {
                val oneMonthAgo = currentTime - 30L * 24 * 60 * 60 * 1000
                transactionsMap.filterKeys { it >= oneMonthAgo }.toMutableMap()
            }

            3 -> {
                val oneYearAgo = currentTime - 365L * 24 * 60 * 60 * 1000
                transactionsMap.filterKeys { it >= oneYearAgo }.toMutableMap()
            }

            else -> transactionsMap // return the original map
        }
    }

    private fun calculateTotalExpense(transactions: List<TransactionEntity>): Double {
        var totalExpense = 0.0
        for (transaction in transactions) {
            val amount = transaction.amount.toDouble()
            totalExpense += amount
        }
        return totalExpense
    }
}

