package com.dailyapps.emberwake.domain

data class RitualStep(
    val id: String,
    val title: String,
    val done: Boolean = false
)

data class Commitment(
    val id: String,
    val title: String,
    val done: Boolean = false,
    val energyCost: Int = 2
)

data class DayLog(
    val date: String,
    val energy: Int,
    val wakeDone: Int,
    val shutDone: Int,
    val commitmentsDone: Int,
    val commitmentsTotal: Int
)

data class EmberState(
    val date: String,
    val energy: Int = 6,
    val wake: List<RitualStep> = defaultWake(),
    val shutdown: List<RitualStep> = defaultShutdown(),
    val commitments: List<Commitment> = emptyList(),
    val history: List<DayLog> = emptyList(),
    val draft: String = ""
)

fun defaultWake(): List<RitualStep> = listOf(
    RitualStep("w1", "Hydrate and open a window"),
    RitualStep("w2", "Write one sentence of intent"),
    RitualStep("w3", "Pick three commitments only"),
    RitualStep("w4", "Silence non-essential notifications"),
    RitualStep("w5", "Start the first deep block")
)

fun defaultShutdown(): List<RitualStep> = listOf(
    RitualStep("s1", "Park unfinished work in one note"),
    RitualStep("s2", "Clear desk and close extra tabs"),
    RitualStep("s3", "Log energy honestly"),
    RitualStep("s4", "Name one win from today"),
    RitualStep("s5", "Set tomorrow's first move")
)
