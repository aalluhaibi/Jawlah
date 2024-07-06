package com.example.jawlah.presentation.feature.myplans

import android.app.Activity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jawlah.R
import com.example.jawlah.presentation.component.MainFab
import com.example.jawlah.presentation.component.PlanCard
import com.example.jawlah.presentation.component.QuickPick
import com.example.jawlah.presentation.feature.destinations.AskGemiScreenDestination
import com.example.jawlah.presentation.feature.destinations.CreatePlanScreenDestination
import com.example.jawlah.presentation.feature.destinations.PlanDetailsScreenDestination
import com.example.jawlah.presentation.util.AppNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow


@AppNavGraph(start = true)
@Destination
@Composable
fun MyPlansScreen(
    myPlansViewModel: MyPlansViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current
    val intent = (context as? Activity)?.intent
    val extraNavigateTo = remember { intent?.getStringExtra("navigateTo") }

    if (extraNavigateTo != null) {
        navigator.navigate(
            AskGemiScreenDestination(QuickPick.LANDMARK_LENS)
        )
    }

    MyPlansScreenContent(
        viewState = myPlansViewModel.viewState.collectAsStateWithLifecycle().value,
        effectFlow = myPlansViewModel.effect,
        onEventSent = myPlansViewModel::handleEvents,
        onNavigationRequested = { request ->
            when (request) {
                MyPlansContract.Effect.Navigation.NavigateToCreateNewPlan -> navigator.navigate(
                    CreatePlanScreenDestination
                )

                MyPlansContract.Effect.Navigation.AskGemi -> navigator.navigate(
                    AskGemiScreenDestination(null)
                )

                is MyPlansContract.Effect.Navigation.NavigateToPlanDetails -> navigator.navigate(
                    PlanDetailsScreenDestination(request.id)
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPlansScreenContent(
    modifier: Modifier = Modifier,
    viewState: MyPlansContract.State,
    effectFlow: Flow<MyPlansContract.Effect>?,
    onEventSent: (MyPlansContract.Event) -> Unit,
    onNavigationRequested: (MyPlansContract.Effect.Navigation) -> Unit,
) {
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
                title = { Text("My plans", textAlign = TextAlign.Center) },
                actions = {
                    IconButton(
                        onClick = {
                            onNavigationRequested(MyPlansContract.Effect.Navigation.AskGemi)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.chat_ai),
                            contentDescription = "Options"
                        )
                    }

                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            MainFab(
                text = "Create a trip",
                icon = Icons.Default.Add,
            ) {
                onNavigationRequested(MyPlansContract.Effect.Navigation.NavigateToCreateNewPlan)
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(viewState.myPlans) { plan ->
                PlanCard(
                    title = plan.name,
                    list = plan.distenations,
                    startDate = plan.startDate,
                    endDate = plan.endDate,
                    onClicked = {
                        onNavigationRequested(
                            MyPlansContract.Effect.Navigation.NavigateToPlanDetails(
                                plan.id
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MyPlansScreenContentPreview() {
    MyPlansScreenContent(
        viewState = MyPlansContract.State(),
        effectFlow = null,
        onEventSent = {},
        onNavigationRequested = {}
    )
}