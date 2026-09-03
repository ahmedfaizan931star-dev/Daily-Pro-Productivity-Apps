package com.dailyapps.northline.domain

enum class EnergyBand { LOW, STEADY, DEEP }

enum class Friction { NONE, INTERRUPTIONS, UNCLEAR, ENERGY, WAITING }

data class Commitment(
    val id: String,
    val title: String,
    val leverage: Int,
    val energy: EnergyBand,
    val friction: Friction,
    val done: Boolean,
    val createdAt: Long
)

data class DayPlan(
    val dateKey: String,
    val northStar: String,
    val commitments: List<Commitment>,
    val closed: Boolean,
    val alignmentScore: Int
)

data class NorthlineState(
    val today: DayPlan,
    val history: List<DayPlan>,
    val weeklyWins: Int,
    val streak: Int
)
