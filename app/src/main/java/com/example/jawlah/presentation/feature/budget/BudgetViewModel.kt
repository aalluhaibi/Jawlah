package com.example.jawlah.presentation.feature.budget

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.jawlah.base.network.ApiResult
import com.example.jawlah.data.local.realm.plan.entity.BudgetEntity
import com.example.jawlah.data.local.realm.plan.entity.TransactionEntity
import com.example.jawlah.domain.budget.usecase.AddTransactionRequest
import com.example.jawlah.domain.budget.usecase.AddTransactionUseCase
import com.example.jawlah.domain.budget.usecase.CreateBudgetRequest
import com.example.jawlah.domain.budget.usecase.CreateBudgetUseCase
import com.example.jawlah.domain.budget.usecase.Request
import com.example.jawlah.domain.budget.usecase.RetrieveBudgetUseCase
import com.example.jawlah.presentation.feature.destinations.BudgetScreenDestination
import com.example.jawlah.presentation.util.BaseMviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val retrieveBudgetUseCase: RetrieveBudgetUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val createBudgetUseCase: CreateBudgetUseCase,
    savedStateHandle: SavedStateHandle
) : BaseMviViewModel<BudgetContract.Event, BudgetContract.State, BudgetContract.Effect>() {
    override fun setInitialState(): BudgetContract.State = BudgetContract.State()

    private val navArgsFromDestination: BudgetScreenNavArgs =
        BudgetScreenDestination.argsFrom(savedStateHandle)

    override fun handleEvents(event: BudgetContract.Event) {
        when (event) {
            BudgetContract.Event.InsertTransaction -> TODO()
            BudgetContract.Event.OnAddTransactionClick -> {
                addTransaction()
            }

            is BudgetContract.Event.OnDeleteTransactionClick -> TODO()
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
            }

            is BudgetContract.Event.OnSelectedFilterChanged -> {
                setState {
                    copy(selectedFilterIndex = event.index)
                }
            }
        }
    }

    private fun addTransaction() {
        viewModelScope.launch {
            addTransactionUseCase(
                AddTransactionRequest(
                    transaction = TransactionEntity().apply {
                        amount = "50.0"
                        description = "Test description"
                        category = "Restaurant"
                        budgetId = viewState.value.budgetId
                    }
                )
            ).collect { result ->

                when (result) {
                    is ApiResult.Error -> {

                    }

                    ApiResult.Loading -> {

                    }

                    ApiResult.Offline -> {}
                    is ApiResult.Success -> {}
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

                        setState {
                            copy(
                                loading = false,
                                budgetId = result.value.id,
                                totalIncome = result.value.totalIncome,
                                totalExpense = result.value.totalExpense,
                                transactions = result.value.transactionEntities.toMutableList(),
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
                totalIncome = 0.0
                totalExpense = 0.0
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
                            copy(loading = false)
                        }
                        retrieveBudget()
                    }
                }
            }
        }
    }
}
