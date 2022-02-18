package net.fearlessplus.ui.main.information

import net.fearlessplus.R
import net.fearlessplus.extensions.toStringFromRes

enum class InformationCategory(var label: String) {
    schedule(R.string.schedule.toStringFromRes),
    chart(R.string.chart.toStringFromRes),
    event(R.string.event.toStringFromRes)
}