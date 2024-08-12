package com.example.jawlah.presentation.feature.plandetails

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jawlah.R
import com.example.jawlah.data.local.realm.plan.entity.PlaceEntity
import com.example.jawlah.data.local.realm.plan.entity.PlaceType
import com.example.jawlah.presentation.component.AddPlaceBottomSheet
import com.example.jawlah.presentation.component.AiSuggestionCard
import com.example.jawlah.presentation.component.FullScreenBottomSheet
import com.example.jawlah.presentation.component.FullScreenDialog
import com.example.jawlah.presentation.component.PlaceEntryDialog
import com.example.jawlah.presentation.component.PlanDetailCard
import com.example.jawlah.presentation.feature.destinations.AITestScreenDestination
import com.example.jawlah.presentation.feature.destinations.BudgetScreenDestination
import com.example.jawlah.presentation.util.SIDE_EFFECTS_KEY
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow

@RequiresApi(Build.VERSION_CODES.O)
@Destination(navArgsDelegate = PlanDetailsScreenNavArgs::class)
@Composable
fun PlanDetailsScreen(
    viewModel: PlanDetailsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    PlanDetailsScreenContent(
        viewState = viewModel.viewState.collectAsStateWithLifecycle().value,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::handleEvents,
        onNavigationRequested = {
            when (it) {
                PlanDetailsContract.Effect.Navigation.Back -> navigator.navigateUp()
                PlanDetailsContract.Effect.Navigation.AITest -> navigator.navigate(
                    AITestScreenDestination
                )

                is PlanDetailsContract.Effect.Navigation.Budget -> {
                    navigator.navigate(
                        BudgetScreenDestination(
                            viewModel.viewState.value.planId
                        )
                    )
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanDetailsScreenContent(
    modifier: Modifier = Modifier,
    viewState: PlanDetailsContract.State,
    effectFlow: Flow<PlanDetailsContract.Effect>?,
    onEventSent: (PlanDetailsContract.Event) -> Unit,
    onNavigationRequested: (PlanDetailsContract.Effect.Navigation) -> Unit,
) {
    LaunchedEffect(SIDE_EFFECTS_KEY) {
        onEventSent(PlanDetailsContract.Event.Init)
        onEventSent(PlanDetailsContract.Event.LoadSuggestions)
    }
    val context = LocalContext.current
    var showPlaceDialog by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showBottomSheetPlaces by remember { mutableStateOf(false) }
    var listType by remember { mutableStateOf(PlaceType.Place) }
    var placeName by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(
                    top = dimensionResource(id = R.dimen.dp8),
                    bottom = dimensionResource(id = R.dimen.dp8)
                ),
                title = { Text("Trip Details") },
                actions = {
                    IconButton(
                        onClick = {
                            Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.chat_ai),
                            contentDescription = "Chat",
                            modifier = modifier.size(24.dp),
                        )
                    }
                    IconButton(
                        onClick = {
                            onNavigationRequested(PlanDetailsContract.Effect.Navigation.AITest)
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.check_box),
                            contentDescription = "Information",
                            modifier = modifier.size(24.dp),
                        )
                    }
                    IconButton(
                        onClick = {
                            onNavigationRequested(PlanDetailsContract.Effect.Navigation.Budget("0"))
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.calculator),
                            contentDescription = "Calculator",
                            modifier = modifier.size(24.dp),
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigationRequested(PlanDetailsContract.Effect.Navigation.Back) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Close"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showPlaceDialog = true
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            if (showPlaceDialog) {
                PlaceEntryDialog(
                    name = placeName,
                    onDismiss = {
                        placeName = ""
                        showPlaceDialog = false
                    }
                ) { name, description, locationUrl, type ->
                    onEventSent(PlanDetailsContract.Event.AddPlace(PlaceEntity().apply {
                        this.name = name
                        this.description = description
                        this.locationUrl = locationUrl
                        this.type = when (type) {
                            "PLACE" -> PlaceType.Place
                            "ACTIVITY" -> PlaceType.Activity
                            "LODGING" -> PlaceType.Lodging
                            "OTHER" -> PlaceType.Other
                            else -> PlaceType.Place
                        }
                    }))
                    placeName = ""
                    showPlaceDialog = false
                }
            }

            if (showBottomSheetPlaces) {
                FullScreenBottomSheet(
                    placeList = when (listType) {
                        PlaceType.Place -> viewState.places
                        PlaceType.Activity -> viewState.activities
                        PlaceType.Lodging -> viewState.lodging
                        PlaceType.Other -> viewState.others
                    }
                ) {
                    showBottomSheetPlaces = false
                }
            }

            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
            ) {

                if (showBottomSheet) {
                    AddPlaceBottomSheet(onDismiss = { showBottomSheet = false }) { placeType ->

                        showDialog = true
                    }
                }

                if (showDialog) {
                    FullScreenDialog()
                }

                Column {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowRight,
                            contentDescription = "Expand",
                            modifier = modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = modifier.width(8.dp))

                        Text(
                            text = "AI Recommendations",
                            style = TextStyle(
                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                                fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                                color = MaterialTheme.colorScheme.primary
                            ),
                        )

                        Spacer(modifier = modifier.weight(1f))

                        Icon(
                            painter = painterResource(R.drawable.ai_2stars),
                            contentDescription = "Expand",
                            modifier = modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    Spacer(modifier = modifier.height(8.dp))
                    AiSuggestionCard(
                        title = "Places",
                        description = "Must-See attractions. Do not miss any thing!",
                        isLoading = viewState.aiLoading,
                        list = viewState.suggestedPlaces,
                        icon = R.drawable.map_location,
                        onItemClicked = {
                            showPlaceDialog = true
                            placeName = it
                        }
                    )

                    Spacer(modifier = modifier.height(16.dp))
                }
            }

            Column(modifier = modifier.fillMaxWidth()) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .heightIn(max = 200.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    PlanDetailCard(
                        title = "Places",
                        icon = R.drawable.map_location,
                        count = viewState.places.size,
                        modifier = modifier.weight(1f)
                    ) {
                        showBottomSheetPlaces = true
                        listType = PlaceType.Place
                    }
                    Spacer(modifier = modifier.width(8.dp))
                    PlanDetailCard(
                        title = "Activities",
                        icon = R.drawable.hot_air_balloon,
                        count = viewState.activities.size,
                        modifier = modifier.weight(1f)
                    ) {
                        showBottomSheetPlaces = true
                        listType = PlaceType.Activity
                    }
                }
                Spacer(modifier = modifier.padding(8.dp))
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .heightIn(max = 200.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    PlanDetailCard(
                        title = "Lodging",
                        icon = R.drawable.lodging,
                        count = viewState.lodging.size,
                        modifier = modifier.weight(1f)
                    ) {
                        showBottomSheetPlaces = true
                        listType = PlaceType.Lodging
                    }
                    Spacer(modifier = modifier.width(8.dp))
                    PlanDetailCard(
                        title = "Others",
                        icon = R.drawable.booking,
                        count = viewState.others.size,
                        modifier = modifier.weight(1f)
                    ) {
                        showBottomSheetPlaces = true
                        listType = PlaceType.Other
                    }
                }
            }
        }
    }
}

data class PlanDetailsScreenNavArgs(
    val id: String,
    val listOfDestinations: String
)


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PlanDetailsScreenContentPreview() {
    PlanDetailsScreenContent(
        viewState = PlanDetailsContract.State(),
        effectFlow = null,
        onEventSent = {},
        onNavigationRequested = {}
    )
}