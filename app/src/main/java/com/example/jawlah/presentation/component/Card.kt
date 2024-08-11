package com.example.jawlah.presentation.component


import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jawlah.R
import com.example.jawlah.presentation.util.ShimmerListItem
import com.example.jawlah.presentation.util.convertLongToDateString

@Composable
fun PlaceCard(modifier: Modifier = Modifier, text: String, onClickListener: () -> Unit) {
    Card(
        modifier = modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .height(32.dp)
                .padding(8.dp)
        ) {
            Text(
                text = text, style = TextStyle(
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    fontWeight = MaterialTheme.typography.labelSmall.fontWeight,
                    fontFamily = MaterialTheme.typography.labelSmall.fontFamily
                ),
                modifier = modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                onClick = {

                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Destination",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@Composable
fun AiSuggestionCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    isLoading: Boolean = true,
    list: List<String> = listOf(),
    icon: Int,
    onItemClicked: (String) -> Unit
) {
    OutlinedCard(
        modifier = modifier.padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
    ) {
        Column(
            modifier = modifier
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "Expand",
                    modifier = modifier.size(24.dp)
                )

                Spacer(modifier = modifier.width(8.dp))

                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                        fontFamily = MaterialTheme.typography.titleSmall.fontFamily
                    ),
                    modifier = modifier.padding(horizontal = 8.dp)
                )
            }

            Text(
                description,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = MaterialTheme.typography.bodySmall.fontWeight,
                    fontFamily = MaterialTheme.typography.bodySmall.fontFamily
                ),
                modifier = modifier.padding(start = 32.dp, end = 8.dp)
            )

            if(isLoading) {
                Row(
                    modifier = modifier.horizontalScroll(state = rememberScrollState())
                ) {
                    repeat(3) {
                        ShimmerListItem(
                            isLoading = isLoading,
                            modifier = modifier.padding(start = 26.dp, top = 8.dp)
                        ) {}
                    }
                }
            }

            LazyRow(
                modifier = modifier.padding(start = 26.dp)
            ) {
                items(list) { item ->
                    AssistChip(
                        onClick = { onItemClicked(item) },
                        label = { Text(item) },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "destination",
                                modifier = modifier.size(AssistChipDefaults.IconSize)
                            )
                        }
                    )
                    Spacer(modifier = modifier.padding(4.dp))
                }
            }

            Spacer(modifier = modifier.height(16.dp))
        }
    }
}


@Composable
fun PlanCard(
    modifier: Modifier = Modifier,
    title: String,
    list: List<String> = listOf(),
    startDate: Long,
    endDate: Long,
    onClicked: (String) -> Unit
) {
    OutlinedCard(
        modifier = modifier.padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(
            modifier = modifier
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.trip_time),
                    contentDescription = "Expand",
                    modifier = modifier.size(32.dp)
                )

                Spacer(modifier = modifier.width(8.dp))

                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                        fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                    ),
                    modifier = modifier.padding(horizontal = 8.dp)
                )
            }

            LazyRow(
                modifier = modifier.padding(start = 26.dp)
            ) {
                items(list) { item ->
                    FilterChip(
                        selected = true,
                        onClick = { onClicked(item) },
                        label = { Text(item) },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.LocationOn,
                                contentDescription = "destination",
                                modifier = modifier.size(AssistChipDefaults.IconSize)
                            )
                        }
                    )
                    Spacer(modifier = modifier.padding(8.dp))
                }
            }


            Row(
                modifier = modifier.padding(horizontal = 26.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text(convertLongToDateString(startDate)) },
                )
                Spacer(modifier = modifier.width(4.dp))
                AssistChip(
                    onClick = { },
                    label = { Text(convertLongToDateString(endDate)) },
                )
            }
        }
    }
}


@Composable
fun PlanDetailCard(
    modifier: Modifier = Modifier,
    title: String,
    icon: Int,
    count: Int = 0,
    onClicked: () -> Unit
) {
    OutlinedCard(
        modifier = modifier.padding(8.dp),
        onClick = {
            onClicked.invoke()
        }
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = title,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = title,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        fontWeight = MaterialTheme.typography.titleSmall.fontWeight,
                        fontFamily = MaterialTheme.typography.titleSmall.fontFamily
                    )
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = count.toString(),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun TripSummaryCard(modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            // Trip Title
            Text(
                text = "New York Trip",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Thu, Dec 17 - Mon, Dec 21, 2020",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Timeline Items
            TimelineItem(time = "06:00 PST AM", title = "SFO - LAX", icon = Icons.Filled.LocationOn, description = "DL 2648 (Delta Air Lines)\nConf: DL1234\nDep. Term. 2, Gate 56B, Seat: 19B\nArrival: 7:29 AM PST")
            Spacer(
                modifier = Modifier
                    .width(2.dp)
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.outline)
            )
            TimelineItem(time = "", title = "36m layover in Los Angeles", icon = Icons.Filled.LocationOn)
            Spacer(
                modifier = Modifier
                    .width(2.dp)
                    .height(40.dp)
                    .background(MaterialTheme.colorScheme.outline)
            )
            TimelineItem(time = "08:05 PST AM", title = "LAX - JFK", icon = Icons.Filled.LocationOn, description = "DL 688 (Delta Air Lines)\nConf: DL1234\nDep. Term. 3, Gate 33, Seat: 12C\nArrival: 4:35 PM EST")
            Spacer(modifier = Modifier.height(8.dp))
            TimelineItem(time = "", title = "Avis Car Rental - JFK", icon = Icons.Filled.LocationOn, description = "Pick up: 7:45 PM EST\nJohn F. Kennedy International Airport (JFK), 305 Federal Ci")
            Spacer(modifier = Modifier.height(8.dp))
            TimelineItem(time = "", title = "Central Park Hotel", icon = Icons.Filled.LocationOn, description = "Check in: 8:00 PM EST\n201 W 58th St. New ")
        }
    }
}

@Composable
fun TimelineItem(time: String, title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, description: String = "") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Timeline Dot
        Box(
            modifier = Modifier
                .size(16.dp)
                .background(color = MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        }

        // Time and Title
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            if (time.isNotEmpty()) {
                Text(
                    text = time,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(bottom = 2.dp),
                    textAlign = TextAlign.Start
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 4.dp),
                textAlign = TextAlign.Start
            )
            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PlaceCardPreview() {
    PlaceCard(text = "Test") {}
}

@Preview(showBackground = true)
@Composable
fun TripSummaryCardPreview() {
    TripSummaryCard()
}

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