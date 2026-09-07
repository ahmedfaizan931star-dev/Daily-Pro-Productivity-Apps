package com.dailyapps.keelstone.domain

data class KeelStone(
    val id: String,
    val title: String,
    val why: String,
    val hoursTarget: Int,
    val hoursLogged: Float,
    val done: Boolean
)

data class Carve(
    val id: String,
    val stoneId: String,
    val dayLabel: String,
    val minutes: Int,
    val note: String
)

data class WeekState(
    val weekLabel: String,
    val capacityHours: Int,
    val stones: List<KeelStone>,
    val carves: List<Carve>,
    val reviewNote: String
) {
    val loggedHours: Float get() = stones.sumOf { it.hoursLogged.toDouble() }.toFloat()
    val loadPercent: Float
        get() = if (capacityHours == 0) 0f else (loggedHours / capacityHours).coerceAtMost(1.5f)
}
