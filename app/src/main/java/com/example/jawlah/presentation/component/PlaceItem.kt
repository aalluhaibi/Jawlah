package com.example.jawlah.presentation.component

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jawlah.R
import com.example.jawlah.data.local.realm.plan.entity.PlaceEntity

@Composable
fun PlaceItem(
    modifier: Modifier = Modifier,
    place: PlaceEntity
) {
    val context = LocalContext.current
    val locationUrl = place.locationUrl
    OutlinedCard(onClick = {
        if (locationUrl.isNotBlank()) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(locationUrl))
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(
                    context,
                    "No application can handle this request.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                "No location is associated with this place",
                Toast.LENGTH_LONG
            ).show()
        }
    }, modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.map_location),
                contentDescription = stringResource(
                    id = R.string.location
                ),
                modifier = modifier.size(32.dp)
            )
            Spacer(modifier = modifier.width(8.dp))
            Column(modifier = modifier.weight(1f)) {
                Text(text = place.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = modifier.height(8.dp))
                Text(text = place.description, style = MaterialTheme.typography.labelMedium)
            }

            Image(
                imageVector = Icons.Default.KeyboardArrowRight, contentDescription = stringResource(
                    id = R.string.details
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceItemPreview(modifier: Modifier = Modifier) {
    PlaceItem(place = PlaceEntity().apply {
        name = "Test"
        description = "Test Description"
    })
}