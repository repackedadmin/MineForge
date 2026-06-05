package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "withdrawals")
data class WithdrawalRequest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,
    val amount: Double,
    val walletAddress: String,
    val network: String,
    val txHash: String,
    val status: String, // "PENDING", "PROCESSING", "COMPLETED", "FAILED"
    val createdAt: Long = System.currentTimeMillis()
)
