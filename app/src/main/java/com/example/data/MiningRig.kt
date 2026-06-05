package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mining_rigs")
data class MiningRig(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userEmail: String,
    val name: String,
    val tierName: String, // STARTER, ADVANCED, PRO, ELITE
    val hashRateMhs: Double,
    val priceUsd: Double,
    val dailyEarnings: Double,
    val purchaseTimestamp: Long = System.currentTimeMillis(),
    val lastClaimTimestamp: Long = System.currentTimeMillis()
)
