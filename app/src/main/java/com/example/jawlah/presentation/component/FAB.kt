package com.example.jawlah.presentation.component

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MainFab(modifier: Modifier = Modifier, text: String, icon: ImageVector, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        onClick = { onClick() },
        icon = { Icon(imageVector = icon, contentDescription = text) },
        text = { Text(text = text) },
    )
}