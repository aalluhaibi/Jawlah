package com.example.jawlah.presentation.component.glance


import android.content.Context
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.jawlah.R
import com.example.jawlah.data.local.realm.plan.entity.BudgetEntity

@Composable
fun BudgetGlanceContent(
    modifier: GlanceModifier = GlanceModifier,
    context: Context,
    budget: BudgetEntity = BudgetEntity()
) {
    if (budget.transactionEntities.isEmpty()) {
        EmptyComponent(context = context)
    } else {
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            Row(
                modifier = modifier.fillMaxWidth()
                    .background(colorProvider = ColorProvider(R.color.glance_income_header_bg))
                    .padding(8.dp)
            ) {
                Text(
                    text = context.getString(R.string.your_budget), style = TextStyle(
                        color = GlanceTheme.colors.onSurface,
                        fontSize = 18.sp
                    )
                )
                Spacer(modifier = modifier.width(16.dp))
                Text(
                    text = budget.totalIncome.toString(), style = TextStyle(
                        color = GlanceTheme.colors.onSurface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = modifier.height(16.dp))
            LazyColumn(modifier = modifier) {
                items(budget.transactionEntities) { item ->
                    Column {
                        Column(
                            modifier = modifier.fillMaxWidth()
                                .background(colorProvider = ColorProvider(R.color.glance_transaction_bg))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = item.amount, style = TextStyle(
                                    color = GlanceTheme.colors.onSurface,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Spacer(modifier = modifier.height(2.dp))
                            Text(
                                text = item.description, style = TextStyle(
                                    color = GlanceTheme.colors.onSurface,
                                    fontSize = 14.sp
                                )
                            )
                        }
                        Spacer(modifier = modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyComponent(modifier: GlanceModifier = GlanceModifier, context: Context) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Vertical.CenterVertically,
        horizontalAlignment = Alignment.Horizontal.CenterHorizontally
    ) {
        Image(
            provider = ImageProvider(R.drawable.empty),
            contentDescription = context.getString(R.string.empty)
        )
        Spacer(modifier = modifier.height(16.dp))
        Text(
            text = context.getString(R.string.empty),
        )
    }
}