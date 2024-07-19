package com.example.jawlah.presentation.feature.ai_test

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jawlah.R
import com.example.jawlah.presentation.feature.ai_test.model.Choice
import com.example.jawlah.presentation.feature.ai_test.model.Question
import com.example.jawlah.presentation.util.SIDE_EFFECTS_KEY
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow


@Destination
@Composable
fun AITestScreen(
    viewModel: AITestViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    AITestScreenContent(
        viewState = viewModel.viewState.collectAsStateWithLifecycle().value,
        effectFlow = viewModel.effect,
        onEventSent = viewModel::handleEvents,
        onNavigationRequested = {
            when (it) {
                AITestContract.Effect.Navigation.Back -> navigator.navigateUp()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AITestScreenContent(
    modifier: Modifier = Modifier,
    viewState: AITestContract.State,
    effectFlow: Flow<AITestContract.Effect>?,
    onEventSent: (AITestContract.Event) -> Unit,
    onNavigationRequested: (AITestContract.Effect.Navigation) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        onEventSent(AITestContract.Event.LoadQuestions)
    }

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
                title = { Text("See what you know!", color = MaterialTheme.colorScheme.primary) },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigationRequested(AITestContract.Effect.Navigation.Back) }
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = viewState.questions.isNotEmpty()) {
                Column {
                    QuestionsContent(
                        questions = viewState.questions,
                        currentQuestionIndex = viewState.currentQuestionIndex
                    ) {
                        onEventSent(it)
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionsContent(
    modifier: Modifier = Modifier,
    questions: MutableList<Question>,
    currentQuestionIndex: Int = 0,
    onEventSent: (AITestContract.Event) -> Unit
) {
    Column {
        Text(
            text = questions[currentQuestionIndex].text,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))
        questions[currentQuestionIndex].choices.forEachIndexed { index, choice ->
            val color =
                if (questions[currentQuestionIndex].answered && choice.isCorrect) Color.Green
                else Color.Gray

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(width = 1.dp, color = color, shape = RoundedCornerShape(8.dp))
                    .clickable {
                        onEventSent(AITestContract.Event.ChoiceSelected(choice, questions[currentQuestionIndex]))
                    }
            ) {
                Text(
                    text = choice.text,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = modifier.padding(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        ThreeDotsIndicator(currentQuestionIndex, questions.size)

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            if (currentQuestionIndex > 0) {
                OutlineCircleIconButton(
                    onClick = {
                        onEventSent(AITestContract.Event.PreviousQuestion)
                    },
                    text = "Previous",
                    icon = R.drawable.previous_arrow
                )
            }
            if (currentQuestionIndex < questions.size - 1) {
                OutlineCircleIconButton(
                    onClick = {
                        onEventSent(AITestContract.Event.NextQuestion)
                    },
                    text = "Next",
                    icon = R.drawable.next_arrow,
                )
            }
        }
    }
}

@Composable
fun ThreeDotsIndicator(currentQuestionIndex: Int, size: Int = 0) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            val selectedIndex =
                if (currentQuestionIndex == 0) 0 else if (currentQuestionIndex == size - 1) 2 else 1

            Box(
                modifier = Modifier
                    .size(if (index == selectedIndex) 12.dp else 8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (index == selectedIndex) Color.Gray else Color.LightGray
                    )
            )
            if (index < 2)
                Spacer(modifier = Modifier.width(2.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AITestScreenContentPreview() {
    AITestScreenContent(
        viewState = AITestContract.State(
            questions = listOf(
                Question(
                    text = "Question 1",
                    choices = listOf(
                        Choice("Choice 1", false),
                        Choice("Choice 2", true)
                    ),
                    answered = false
                ),
                Question(
                    text = "Question 2",
                    choices = listOf(
                        Choice("Choice 1", false),
                        Choice("Choice 2", true)
                    ),
                    answered = false
                )
            ).toMutableList()
        ),
        effectFlow = null,
        onEventSent = {},
        onNavigationRequested = {}
    )
}

@Composable
fun OutlineCircleIconButton(
    onClick: () -> Unit,
    text: String,
    icon: Int,
    borderColor: Color = Color.Black,
    borderWidth: Dp = 1.dp,
    buttonSize: Dp = 48.dp
) {
    Box(
        modifier = Modifier
            .size(buttonSize)
            .clip(CircleShape)
            .border(borderWidth, borderColor, CircleShape)
            .clickable { onClick() }
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.Center)
        )
    }
}