package com.example.jawlah.presentation.feature.budget

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jawlah.R
import com.example.jawlah.data.local.realm.plan.entity.TransactionEntity
import com.example.jawlah.presentation.component.CategoryDialog
import com.example.jawlah.presentation.component.ExpenseEntryDialog
import com.example.jawlah.presentation.component.MainFab
import com.example.jawlah.presentation.feature.myplans.MyPlansContract
import com.example.jawlah.presentation.util.SIDE_EFFECTS_KEY
import com.example.jawlah.presentation.util.convertLongToDateString
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@RequiresApi(Build.VERSION_CODES.O)
@Destination(navArgsDelegate = BudgetScreenNavArgs::class)
@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {

    BudgetScreenContent(
        viewState = viewModel.viewState.collectAsStateWithLifecycle().value,
        effectFlow = viewModel.effect,
        onEvent = viewModel::handleEvents,
        onNavigationRequested = { request ->
            when (request) {
                is BudgetContract.Effect.Navigation.Back -> navigator.popBackStack()
            }
        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreenContent(
    modifier: Modifier = Modifier,
    viewState: BudgetContract.State,
    effectFlow: Flow<BudgetContract.Effect>,
    onEvent: (BudgetContract.Event) -> Unit,
    onNavigationRequested: (BudgetContract.Effect.Navigation) -> Unit
) {

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        onEvent(BudgetContract.Event.LoadBudget)
    }
    var showTransactionDialog by remember { mutableStateOf(false) }
    var showCategoryDialog by remember { mutableStateOf(false) }
    var showIncomeDialog by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets(
                    top = dimensionResource(id = R.dimen.dp8),
                    bottom = dimensionResource(id = R.dimen.dp8)
                ),
                title = { Text("Wanderlust Wallet") },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigationRequested(BudgetContract.Effect.Navigation.Back) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            showCategoryDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Options"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            MainFab(
                text = "Add transaction",
                icon = Icons.Default.Add,
            ) {
                showTransactionDialog = true
            }
        },
    ) { paddingValues ->

        if (showCategoryDialog) {
            CategoryDialog(
                label = stringResource(R.string.enter_category),
                keyboardType = KeyboardType.Text,
                onDismissRequest = { showCategoryDialog = false },
                onConfirm = { category ->
                    showCategoryDialog = false
                    onEvent(BudgetContract.Event.InsertCategory(category))
                }
            )
        }

        if (showIncomeDialog) {
            CategoryDialog(
                label = stringResource(R.string.enter_income),
                keyboardType = KeyboardType.Number,
                onDismissRequest = { showIncomeDialog = false },
                onConfirm = { income ->
                    showIncomeDialog = false
                    onEvent(BudgetContract.Event.AddIncome(income))
                }
            )
        }

        if (showTransactionDialog) {
            ExpenseEntryDialog(
                categories = viewState.categories.map { it.name },
                onDismiss = {
                    showTransactionDialog = false
                }
            ) { amount, description, selectedCategory, selectedDate, selectedTime ->
                onEvent(BudgetContract.Event.OnAddTransactionClick(TransactionEntity().apply {
                    this.transactionType = TransactionType.EXPENSE
                    this.amount = amount
                    this.description = description
                    this.category = selectedCategory
                    this.date = selectedDate
                    this.time = selectedTime
                }))
                showTransactionDialog = false
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    elevation = CardDefaults.elevatedCardElevation()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = viewState.totalExpense.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.expenses),
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { viewState.expensePercentage },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    elevation = CardDefaults.elevatedCardElevation(),
                    onClick = {
                        showIncomeDialog = true
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = viewState.totalIncome.toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.income),
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { viewState.incomePercentage },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val options = listOf("All", "Week", "Month", "Year")
            SingleChoiceSegmentedButtonRow(
                modifier = modifier.fillMaxWidth()
            ) {
                options.forEachIndexed { index, option ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        onClick = {
                            onEvent(BudgetContract.Event.OnSelectedFilterChanged(index))
                        },
                        selected = index == viewState.selectedFilterIndex
                    ) {
                        Text(text = option)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewState.filteredTransactions.entries.forEach { (key, value) ->
                    item {
                        FilterChip(selected = true, onClick = {}, label = {
                            Text(text = convertLongToDateString(key))
                        }
                        )
                    }

                    items(value) { transaction ->
                        TransactionItem(transaction) {
                            onEvent(BudgetContract.Event.OnDeleteTransactionClick(transaction))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: TransactionEntity,
    onItemClicked: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column {
            val amount =
                "-${transaction.amount}"
            Text(
                text = amount,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
            Text(
                text = transaction.description,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            )
            Text(
                text = transaction.category,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { onItemClicked() }) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(id = R.string.delete)
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun BudgetScreenContentPreview() {
    BudgetScreenContent(
        viewState = BudgetContract.State(
            transactionsMap = mutableMapOf(
                102435L to listOf(TransactionEntity().apply {
                    amount = "100.00"
                    description = "Launch"
                    category = "Food"
                }, TransactionEntity().apply {
                    amount = "90.00"
                    description = "Dinner"
                    category = "Food"
                    transactionType = TransactionType.EXPENSE
                }),
                102433L to listOf(TransactionEntity().apply {
                    amount = "100.00"
                    description = "Food"
                    category = "Food Test"
                }, TransactionEntity().apply {
                    amount = "90.00"
                    description = "Food Test"
                    category = "Food"
                    transactionType = TransactionType.EXPENSE
                })
            )
        ),
        effectFlow = emptyFlow(),
        onEvent = {},
        onNavigationRequested = {}
    )
}

data class BudgetScreenNavArgs(
    val id: String
)