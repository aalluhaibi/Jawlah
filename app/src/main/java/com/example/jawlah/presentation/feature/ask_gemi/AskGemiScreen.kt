package com.example.jawlah.presentation.feature.ask_gemi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jawlah.R
import com.example.jawlah.presentation.component.ChatBottomBar
import com.example.jawlah.presentation.component.ChatBubble
import com.example.jawlah.presentation.component.LottieAnimationComponent
import com.example.jawlah.presentation.component.QuickPick
import com.example.jawlah.presentation.util.AppNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow

@AppNavGraph
@Destination(navArgsDelegate = AskGemiScreenNavArgs::class)
@Composable
fun AskGemiScreen(
    viewModel: AskGemiViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    AskGemiScreenContent(
        viewState = viewModel.viewState.collectAsStateWithLifecycle().value,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::handleEvents,
        onNavigationRequested = { request ->
            when (request) {
                AskGemiContract.Effect.Navigation.Back -> navigator.navigateUp()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AskGemiScreenContent(
    modifier: Modifier = Modifier,
    viewState: AskGemiContract.State,
    effectFlow: Flow<AskGemiContract.Effect>?,
    onEventSent: (AskGemiContract.Event) -> Unit,
    onNavigationRequested: (AskGemiContract.Effect.Navigation) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()

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
                title = { Text("Ask Gemi") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onNavigationRequested(AskGemiContract.Effect.Navigation.Back)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                reverseLayout = true,
                state = listState,
                modifier = modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {
                items(viewState.messages.reversed()) { message ->
                    ChatBubble(message = message)
                }
            }

            if (viewState.loading) {
                Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    LottieAnimationComponent(
                        modifier = modifier
                            .width(100.dp)
                            .height(50.dp),
                        resId = R.raw.typing
                    )
                }
            }
            ChatBottomBar(
                text = viewState.textMessage,
                onTextChanged = { onEventSent(AskGemiContract.Event.OnTextMessageChanged(it)) },
                onSubmit = { msg -> onEventSent(AskGemiContract.Event.SubmitGeneralMessage(msg)) },
            ) { msg, quickPick ->
                when (quickPick) {
                    QuickPick.LUGGAGE_CLASSIFICATION -> {
                        onEventSent(AskGemiContract.Event.SubmitLuggageClassificationMessage(msg))
                    }

                    QuickPick.READ_GATE_NO -> {}
                    QuickPick.LANDMARK_LENS -> {
                        onEventSent(AskGemiContract.Event.SubmitLandmarkLensMessage(msg))
                    }

                    else -> {}
                }
            }
        }
    }
}

data class AskGemiScreenNavArgs(
    val quickPick: QuickPick?
)