package com.dailyapps.horizonledger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val title: String,
    val category: String,
    val note: String = "",
    val isExpense: Boolean = true,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val limitAmount: Double,
    val period: String = "monthly"
)

@Entity(tableName = "saving_goals")
data class SavingGoal(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val deadline: Long? = null
)

data class CategorySummary(
    val category: String,
    val total: Double,
    val count: Int
)

data class DashboardStats(
    val todaySpent: Double = 0.0,
    val weekSpent: Double = 0.0,
    val monthSpent: Double = 0.0,
    val todayIncome: Double = 0.0,
    val budgetRemaining: Double = 0.0,
    val topCategory: String = "",
    val transactionCount: Int = 0
)

val DEFAULT_CATEGORIES = listOf(
    "Food & Drink",
    "Transport",
    "Shopping",
    "Bills",
    "Entertainment",
    "Health",
    "Education",
    "Travel",
    "Salary",
    "Other"
)
