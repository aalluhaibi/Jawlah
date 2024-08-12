package com.example.jawlah.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jawlah.R

@Composable
fun DestinationTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Destinations",
    destinations: MutableList<String>,
    onAddClick: () -> Unit,
    onRemoveClick: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Pin",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = onAddClick, colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Destination",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            label = { Text(label) },
            modifier = modifier
                .fillMaxWidth()
        )

        AnimatedVisibility(destinations.size > 0) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .horizontalScroll(state = scrollState),
                horizontalArrangement = Arrangement.Start
            ) {
                destinations.forEach {
                    CustomInputChip(text = it) {
                        onRemoveClick(destinations.indexOf(it))
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}


@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
    )
}


@Composable
fun CustomInputChip(
    text: String,
    onDismiss: () -> Unit,
) {
    var enabled by remember { mutableStateOf(true) }
    if (!enabled) return

    InputChip(
        onClick = {
            onDismiss()
            enabled = !enabled
        },
        label = { Text(text) },
        selected = enabled,
        trailingIcon = {
            Icon(
                Icons.Default.Close,
                contentDescription = "Cancel destination",
                Modifier.size(InputChipDefaults.AvatarSize)
            )
        },
    )
}

@Composable
fun CustomMandatoryDateTextField(
    modifier: Modifier = Modifier,
    title: String,
    hint: String,
    text: String = "",
    onClicked: () -> Unit
) {
    Row {
        Text(
            text = stringResource(id = R.string.mandatory),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Color(0xFFB42318)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            modifier = modifier.clickable { onClicked() },
            text = title,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    val interactionSource = remember { MutableInteractionSource() }
    Box {
        OutlinedTextField(
            value = text,
            onValueChange = { },
            modifier = modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { onClicked() })
                },
            interactionSource = interactionSource,
            placeholder = {
                Text(
                    text = hint,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                )
            },
            shape = RoundedCornerShape(8.dp)
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(onClick = onClicked)
        )
    }
}