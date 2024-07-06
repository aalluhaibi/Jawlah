package com.example.jawlah.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jawlah.R

@Composable
fun PickImageBottomSheet(
    onDismiss: () -> Unit,
    onTakePhotoClick: (QuickPick?) -> Unit,
    onPhotoGalleryClick: () -> Unit,
) {
    MyModalBottomSheetContent(
        header = "Choose Option",
        onDismiss = {
            onDismiss.invoke()
        },
        items = listOf(
            BottomSheetItem(
                title = "Take Photo",
                icon = painterResource(R.drawable.camera),
                onClick = {
                    onTakePhotoClick.invoke(null)
                }
            ),
            BottomSheetItem(
                title = "select image",
                icon = painterResource(R.drawable.gallery),
                onClick = {
                    onPhotoGalleryClick.invoke()
                }
            ),

            BottomSheetItem(
                title = "Luggage classifications",
                icon = painterResource(R.drawable.luggage_classifications),
                onClick = {
                    onTakePhotoClick.invoke(QuickPick.LUGGAGE_CLASSIFICATION)
                }
            ),
            BottomSheetItem(
                title = "Read Gate No.",
                icon = painterResource(R.drawable.gate),
                onClick = {
                    onTakePhotoClick.invoke(QuickPick.READ_GATE_NO)
                }
            ),
            BottomSheetItem(
                title = "Landmark Lens",
                icon = painterResource(R.drawable.landmark),
                onClick = {
                    onTakePhotoClick.invoke(QuickPick.LANDMARK_LENS)
                }
            )
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyModalBottomSheetContent(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    //header
    header: String = "Choose Option",

    items: List<BottomSheetItem> = listOf(),
) {
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )
    val edgeToEdgeEnabled by remember { mutableStateOf(false) }
    val windowInsets = if (edgeToEdgeEnabled)
        WindowInsets(0) else BottomSheetDefaults.windowInsets

    ModalBottomSheet(
        shape = MaterialTheme.shapes.medium.copy(
            bottomStart = CornerSize(0),
            bottomEnd = CornerSize(0)
        ),
        onDismissRequest = { onDismiss.invoke() },
        sheetState = bottomSheetState,
        windowInsets = windowInsets
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = header,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (item in items.take(2)) {
                    Column(
                        modifier = modifier
                            .clickable {
                            item.onClick.invoke()
                        },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = item.icon,
                            contentDescription = item.title,
                            modifier = modifier.size(24.dp)
                        )
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
            Spacer(modifier = modifier.height(8.dp))
            Spacer(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outline)
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                text = "Quick Picks",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            for (item in items.subList(2, items.size)) {
                androidx.compose.material3.ListItem(
                    modifier = modifier.clickable {
                        item.onClick.invoke()
                    },
                    headlineContent = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    },
                    leadingContent = {
                        Icon(
                            painter = item.icon,
                            contentDescription = item.title,
                            modifier = modifier.size(24.dp)
                        )
                    },
                )
            }
        }
    }
}

data class BottomSheetItem(
    val title: String = "",
    val icon: Painter,
    val onClick: () -> Unit
)

@Preview(showBackground = true)
@Composable
fun MyModalBottomSheetContentPreview(modifier: Modifier = Modifier) {
    MyModalBottomSheetContent(
        onDismiss = {},
        items = listOf(
            BottomSheetItem(
                title = "Take Photo",
                icon = painterResource(R.drawable.camera),
                onClick = {}
            ),
            BottomSheetItem(
                title = "select image",
                icon = painterResource(R.drawable.gallery),
                onClick = {}
            ),
            BottomSheetItem(
                title = "Luggage classifications",
                icon = painterResource(R.drawable.luggage_classifications),
                onClick = {}
            ),
            BottomSheetItem(
                title = "Read Gate No.",
                icon = painterResource(R.drawable.gate),
                onClick = {}
            ),
            BottomSheetItem(
                title = "Landmarks",
                icon = painterResource(R.drawable.landmark),
                onClick = {}
            )
        )
    )
}

enum class QuickPick {
    LUGGAGE_CLASSIFICATION,
    READ_GATE_NO,
    LANDMARK_LENS
}