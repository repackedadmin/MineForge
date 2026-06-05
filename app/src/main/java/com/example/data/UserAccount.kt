package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserAccount(
    @PrimaryKey val email: String,
    val displayName: String,
    val passwordHash: String, // Simulating password hashing / authentication
    val hasBiometric: Boolean = false,
    val preferredCurrency: String = "USD",
    val preferredLanguage: String = "EN",
    val connectedWalletAddress: String? = null,
    val walletNetwork: String? = null,
    val mainBalance: Double = 0.0, // Withdrawable balance (MGT)
    val totalMinedLifetime: Double = 0.0,
    val isLoggedIn: Boolean = false,
    val stakedBalance: Double = 0.0, // Staking engine active balance
    val lastStakeTimestamp: Long = 0L // Keep track of accrual periods
)
