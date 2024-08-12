package com.example.jawlah.presentation.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.jawlah.presentation.feature.ask_gemi.Message
import com.example.jawlah.presentation.feature.ask_gemi.Participant

@Composable
fun ChatBubble(
    modifier: Modifier = Modifier,
    message: Message
) {
    val isGemiMessage = message.participant == Participant.GEMI ||
            message.participant == Participant.ERROR

    val backgroundColor = when (message.participant) {
        Participant.GEMI -> MaterialTheme.colorScheme.primaryContainer
        Participant.USER -> MaterialTheme.colorScheme.surfaceContainer
        Participant.ERROR -> MaterialTheme.colorScheme.errorContainer
    }

    val messageShape = if (isGemiMessage) {
        RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
    }

    val horizontalAlignment = if (isGemiMessage) {
        Alignment.Start
    } else {
        Alignment.End
    }

    Column(
        horizontalAlignment = horizontalAlignment,
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = message.participant.name,
            style = MaterialTheme.typography.bodySmall,
            modifier = modifier.padding(bottom = 4.dp)
        )
        Row {
            BoxWithConstraints {
                Card(
                    colors = CardDefaults.cardColors(containerColor = backgroundColor),
                    shape = messageShape,
                    modifier = modifier.widthIn(0.dp, maxWidth * 0.9f)
                ) {
                    Column(
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Text(
                            text = message.text,
                            modifier = Modifier.padding(16.dp)
                        )

                        if (message.img != null) {
                            AsyncImage(
                                model = message.img,
                                modifier = modifier
                                    .wrapContentWidth()
                                    .heightIn(90.dp, 365.dp),
                                contentScale = ContentScale.Fit,
                                contentDescription = "selected image",
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatBubblePreview() {
    Column {
        ChatBubble(
            message = Message(
                text = "Hey there, traveler!",
                participant = Participant.GEMI
            )
        )

        ChatBubble(
            message = Message(
                text = "Hey there, traveler!",
                participant = Participant.USER
            )
        )
    }
}