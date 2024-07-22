package com.example.jawlah.presentation.feature.budget

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jawlah.R
import com.example.jawlah.data.local.realm.plan.entity.TransactionEntity
import com.example.jawlah.presentation.component.MainFab
import com.example.jawlah.presentation.util.SIDE_EFFECTS_KEY
import com.example.jawlah.presentation.util.convertLongToDateString
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

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
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            MainFab(
                text = "Add transaction",
                icon = Icons.Default.Add,
            ) {
                onEvent(BudgetContract.Event.OnAddTransactionClick)
            }
        },
    ) { paddingValues ->
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
                            text = "$434.00",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Expenses",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.5f },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
                OutlinedCard(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    elevation = CardDefaults.elevatedCardElevation()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "$250.00",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Income",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { 0.8f },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val options = listOf("All", "Week", "Month", "Year")
            SingleChoiceSegmentedButtonRow {
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
                viewState.transactionsMap.entries.forEach { (key, value) ->
                    item {
                        FilterChip(selected = true, onClick = {}, label = {
                            Text(text = convertLongToDateString(key))
                        }
                        )
                    }

                    items(value) { transaction ->
                        TransactionItem(transaction)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionEntity) {
    Column {
        val amount =
            if (transaction.transactionType == TransactionType.EXPENSE) "-${transaction.amount}" else "+${transaction.amount}"
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
}


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