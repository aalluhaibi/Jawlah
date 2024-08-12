package com.example.jawlah.presentation.component

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.jawlah.presentation.feature.ask_gemi.Message
import com.example.jawlah.presentation.feature.ask_gemi.Participant

@Composable
fun ChatBottomBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    onSubmit: (Message) -> Unit,
    quickPickSelected: (Message, QuickPick?) -> Unit,
) {
    val uri = remember { mutableStateOf<Uri?>(null) }
    val picture = remember { mutableStateOf<Bitmap?>(null) }

    Column(
        modifier = modifier.imePadding(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TakePictureIcon(
                onSetUri = { uri.value = it },
                onPicReceived = { bitmap, quickPick ->
                    picture.value = bitmap
                    quickPickSelected(
                        Message(
                            text = text,
                            img = picture.value,
                            participant = Participant.USER
                        ), quickPick
                    )
                }
            )

            OutlinedTextField(
                value = text,
                onValueChange = onTextChanged,
                placeholder = { Text("Type your message...") },
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            val context = LocalContext.current
            IconButton(
                onClick = {
                    val image =
                        if (picture.value != null) picture.value else if (uri.value != null) getBitmapFromUri(
                            uri.value!!, context
                        ) else null
                    onSubmit(
                        Message(
                            text = text,
                            img = image,
                            participant = Participant.USER
                        )
                    )
                    uri.value = null
                    picture.value = null
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send Message",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (picture.value != null) {
            AsyncImage(
                model = picture.value,
                modifier = Modifier.size(
                    160.dp
                ),
                contentDescription = "selected image",
            )
        } else if (uri.value != null) {
            AsyncImage(
                model = uri.value,
                modifier = Modifier.size(
                    160.dp
                ),
                contentDescription = "selected image",
            )
        }
    }
}

fun getBitmapFromUri(uri: Uri, context: Context): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


@Preview(showBackground = true)
@Composable
fun ChatBottomBarPreview() {
    ChatBottomBar(
        text = "",
        onTextChanged = {},
        quickPickSelected = { msg, quickPick -> },
        onSubmit = {}
    )
}