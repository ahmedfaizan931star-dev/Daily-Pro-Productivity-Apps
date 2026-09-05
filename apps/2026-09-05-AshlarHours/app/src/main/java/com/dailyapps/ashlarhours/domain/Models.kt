package com.dailyapps.ashlarhours.domain

enum class StoneKind(val label: String, val shortLabel: String) {
    DEEP("Deep work", "Deep"),
    ADMIN("Admin / ops", "Admin"),
    RECOVERY("Recovery", "Rest"),
    SOCIAL("People", "People"),
    EMPTY("Open hour", "Open")
}

data class HourStone(
    val id: String,
    val hour: Int,
    val kind: StoneKind,
    val title: String,
    val done: Boolean = false
)

data class DayPlan(
    val dayIndex: Int,
    val stones: List<HourStone>
)

data class Insights(
    val deepHours: Int,
    val adminHours: Int,
    val recoveryHours: Int,
    val socialHours: Int,
    val completed: Int,
    val planned: Int,
    val balanceNote: String
)
