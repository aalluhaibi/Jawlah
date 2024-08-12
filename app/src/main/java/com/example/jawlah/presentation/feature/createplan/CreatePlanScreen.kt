package com.example.jawlah.presentation.feature.createplan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jawlah.presentation.component.DestinationTextField
import com.example.jawlah.presentation.component.MainFab
import com.example.jawlah.presentation.component.CustomTextField
import com.example.jawlah.presentation.feature.destinations.PlanDetailsScreenDestination
import com.example.jawlah.presentation.feature.plandetails.PlanDetailsScreenNavArgs
import com.example.jawlah.presentation.util.AppNavGraph
import com.example.jawlah.presentation.util.SIDE_EFFECTS_KEY
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@AppNavGraph
@Destination
@Composable
fun CreatePlanScreen(
    viewModel: CreatePlanViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    CreatePlanScreenContent(
        viewState = viewModel.viewState.collectAsStateWithLifecycle().value,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::handleEvents,
        onNavigationRequested = { request ->
            when (request) {
                CreatePlanContract.Effect.Navigation.Back -> navigator.navigateUp()
                is CreatePlanContract.Effect.Navigation.NavigateToPlanDetailScreen -> {
                    navigator.navigate(
                        PlanDetailsScreenDestination(PlanDetailsScreenNavArgs(request.id, request.listOfDestinations.toString()))
                    ) {
                        navigator.popBackStack()
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlanScreenContent(
    modifier: Modifier = Modifier,
    viewState: CreatePlanContract.State,
    effectFlow: Flow<CreatePlanContract.Effect>?,
    onEventSent: (CreatePlanContract.Event) -> Unit,
    onNavigationRequested: (CreatePlanContract.Effect.Navigation) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val dateRangePickerState = rememberDateRangePickerState(initialDisplayMode = DisplayMode.Picker)

    LaunchedEffect(
        key1 = dateRangePickerState.selectedStartDateMillis,
        key2 = dateRangePickerState.selectedEndDateMillis
    ) {
        dateRangePickerState.selectedStartDateMillis?.let {
            onEventSent(CreatePlanContract.Event.onStartDateChange(it))
        } ?: onEventSent(CreatePlanContract.Event.onStartDateChange(0L))
        dateRangePickerState.selectedEndDateMillis?.let {
            onEventSent(CreatePlanContract.Event.onEndDateChange(it))
        } ?: onEventSent(CreatePlanContract.Event.onEndDateChange(0L))
    }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is CreatePlanContract.Effect.Error -> {

                }

                CreatePlanContract.Effect.Navigation.Back -> {
                }

                is CreatePlanContract.Effect.Navigation.NavigateToPlanDetailScreen -> {
                    onNavigationRequested(effect)
                }
            }
        }?.collect()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Trip time! ✈\uFE0F") },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigationRequested(CreatePlanContract.Effect.Navigation.Back) }
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
            if (viewState.displayContinueButton) {
                MainFab(
                    text = "Continue",
                    icon = Icons.Default.PlayArrow,
                ) {
                    onEventSent(CreatePlanContract.Event.SavePlan)
                }
            }
        },
    ) { paddingValues ->
        Surface(modifier = modifier.padding(paddingValues)) {
            Column(
                modifier = modifier.padding(16.dp)
            ) {
                CustomTextField(
                    value = viewState.name,
                    onValueChange = { onEventSent(CreatePlanContract.Event.OnNameChange(it)) },
                    label = "Trip name"
                )

                Spacer(modifier = Modifier.height(8.dp))

                DestinationTextField(
                    value = viewState.destination,
                    onValueChange = {
                        onEventSent(CreatePlanContract.Event.onDestinationChange(it))
                    },
                    destinations = viewState.destinations,
                    onAddClick = { onEventSent(CreatePlanContract.Event.AddDestination(viewState.destination)) },
                    onRemoveClick = { onEventSent(CreatePlanContract.Event.RemoveDestination(it)) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                DateRangePicker(
                    state = dateRangePickerState,
                    title = {
                    },
                    headline = {
                        Text("pick your dates!")
                    }
                )
            }
        }
    }
}