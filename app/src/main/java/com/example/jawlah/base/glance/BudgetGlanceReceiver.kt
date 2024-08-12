package com.example.jawlah.base.glance

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class BudgetGlanceReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = BudgetGlance()

}