import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.jawlah.R
import com.example.jawlah.presentation.component.AiSuggestionCard
import com.example.jawlah.presentation.component.PlanCard
import com.example.jawlah.presentation.component.PlanDetailCard

@Preview(showBackground = true)
@Composable
fun AiSuggestionCardPreview(modifier: Modifier = Modifier) {
    AiSuggestionCard(
        modifier = modifier,
        title = "Activities",
        description = "Spice up your itinerary with these local experiences!",
        list = listOf("Test", "Test", "Test"),
        icon = R.drawable.hot_air_balloon,
        onItemClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PlanCardPreview() {
    PlanCard(
        modifier = Modifier,
        title = "My Trip",
        list = listOf("Test", "Test", "Test"),
        startDate = 1718772522L,
        endDate = 1718772522L,
    ) { }
}

@Preview(showBackground = true)
@Composable
fun PlanDetailCardPreview() {
    PlanDetailCard(
        modifier = Modifier,
        title = "Places",
        icon = R.drawable.map_location,
        count = 6
    ) { }
}