package com.example.jawlah.base.glance

import android.content.Context
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.provideContent
import com.example.jawlah.R
import com.example.jawlah.data.local.glance.GlanceRepository
import com.example.jawlah.presentation.component.glance.BudgetGlanceContent

class BudgetGlance : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repo = GlanceRepository.get(context)
        provideContent {
            GlanceTheme {
                Scaffold(
                    titleBar = {
                        TitleBar(
                            startIcon = ImageProvider(R.drawable.calculator),
                            title = context.getString(R.string.trip_budget_label)
                        )
                    }
                ) {
                    BudgetGlanceContent(modifier = GlanceModifier, context = context, budget = repo.retrieveBudget())
                }
            }
        }
    }
}