package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class MiningRepository(private val dao: MineForgeDao) {

    // --- Active User Sessions ---
    val activeUser: Flow<UserAccount?> = dao.observeActiveUser()

    suspend fun getActiveUserSync(): UserAccount? = dao.getActiveUser()

    suspend fun findUserByEmail(email: String): UserAccount? = dao.getUserByEmail(email)

    fun observeUserByEmail(email: String): Flow<UserAccount?> = dao.observeUserByEmail(email)

    suspend fun registerUser(user: UserAccount): Boolean {
        val existing = dao.getUserByEmail(user.email)
        if (existing != null) return false // User already exists
        dao.insertUser(user)
        return true
    }

    suspend fun loginUser(email: String, passwordHash: String): Boolean {
        val user = dao.getUserByEmail(email) ?: return false
        if (user.passwordHash == passwordHash) {
            // Logout any other active accounts first
            dao.logoutAllUsers()
            // Mark this user as active
            dao.updateUser(user.copy(isLoggedIn = true))
            return true
        }
        return false
    }

    suspend fun logoutActiveUser() {
        dao.logoutAllUsers()
    }

    suspend fun updateUserSettings(
        email: String,
        newDisplayName: String,
        currency: String,
        language: String,
        hasBiometric: Boolean
    ) {
        val user = dao.getUserByEmail(email) ?: return
        dao.updateUser(user.copy(
            displayName = newDisplayName,
            preferredCurrency = currency,
            preferredLanguage = language,
            hasBiometric = hasBiometric
        ))
    }

    suspend fun updateWallet(email: String, address: String?, network: String?) {
        val user = dao.getUserByEmail(email) ?: return
        dao.updateUser(user.copy(
            connectedWalletAddress = address,
            walletNetwork = network
        ))
    }

    suspend fun deleteUserAccount(email: String) {
        dao.deleteRigsForUser(email)
        dao.deleteWithdrawalsForUser(email)
        dao.deleteUserByEmail(email)
    }

    // --- Mining Rigs Engine ---
    fun observeRigsForUser(email: String): Flow<List<MiningRig>> = dao.observeRigsForUser(email)

    suspend fun buyRig(email: String, tierName: String, priceUsd: Double, hashRateMhs: Double, dailyEarnings: Double): Boolean {
        val user = dao.getUserByEmail(email) ?: return false
        
        // Construct a unique and distinctive rig name
        val randomTag = (1000..9999).random()
        val rigName = "MineForge $tierName #$randomTag"
        
        val rig = MiningRig(
            userEmail = email,
            name = rigName,
            tierName = tierName,
            hashRateMhs = hashRateMhs,
            priceUsd = priceUsd,
            dailyEarnings = dailyEarnings,
            purchaseTimestamp = System.currentTimeMillis(),
            lastClaimTimestamp = System.currentTimeMillis()
        )
        dao.insertRig(rig)
        return true
    }

    /**
     * Claims all pending earnings from individual active rigs and converts them to the main user wallet balance.
     */
    suspend fun claimPendingEarnings(email: String): Double {
        val user = dao.getUserByEmail(email) ?: return 0.0
        val rigs = dao.getRigsForUser(email)
        if (rigs.isEmpty()) return 0.0

        val now = System.currentTimeMillis()
        var totalClaimed = 0.0

        // Calculate and claim from each rig
        rigs.forEach { rig ->
            val elapsedMs = now - rig.lastClaimTimestamp
            if (elapsedMs > 0) {
                // dailyEarnings are for 24 hours (86,400,000 ms)
                val mgtEarned = rig.dailyEarnings * (elapsedMs.toDouble() / 86400000.0)
                totalClaimed += mgtEarned
                
                // Update rig checkpoint
                dao.updateRig(rig.copy(lastClaimTimestamp = now))
            }
        }

        if (totalClaimed > 0) {
            val updatedUser = user.copy(
                mainBalance = user.mainBalance + totalClaimed,
                totalMinedLifetime = user.totalMinedLifetime + totalClaimed
            )
            dao.updateUser(updatedUser)
        }

        return totalClaimed
    }


    // --- Withdrawals & Transaction History ---
    fun observeWithdrawals(email: String): Flow<List<WithdrawalRequest>> = dao.observeWithdrawalsForUser(email)

    suspend fun executeWithdrawal(email: String, amount: Double, walletAddress: String, network: String): Boolean {
        val user = dao.getUserByEmail(email) ?: return false
        if (user.mainBalance < amount) return false // Insufficient funds

        // Deduct from balance
        val updatedUser = user.copy(mainBalance = user.mainBalance - amount)
        dao.updateUser(updatedUser)

        // Generate a mock authentic Ethereum/BSC tx hash: 0x followed by 64 hexadecimal characters
        val chars = "0123456789abcdef"
        val syntheticHash = "0x" + (1..64).map { chars[Random.nextInt(chars.length)] }.joinToString("")

        // Create transaction entry in PROCESSING state
        val req = WithdrawalRequest(
            userEmail = email,
            amount = amount,
            walletAddress = walletAddress,
            network = network,
            txHash = syntheticHash,
            status = "PROCESSING",
            createdAt = System.currentTimeMillis()
        )
        dao.insertWithdrawal(req)
        return true
    }

    suspend fun stakeTokens(email: String, amount: Double): Boolean {
        val user = dao.getUserByEmail(email) ?: return false
        if (user.mainBalance < amount || amount <= 0) return false
        
        // Calculate accrued rewards from past staking before updating principal, if any
        val now = System.currentTimeMillis()
        val elapsedMs = if (user.lastStakeTimestamp > 0) now - user.lastStakeTimestamp else 0L
        val yearMs = 31536000000.0
        val interest = if (elapsedMs > 0) user.stakedBalance * (elapsedMs.toDouble() / yearMs) * 0.18 else 0.0

        val updatedUser = user.copy(
            mainBalance = user.mainBalance - amount,
            stakedBalance = user.stakedBalance + amount + interest,
            lastStakeTimestamp = now
        )
        dao.updateUser(updatedUser)
        return true
    }

    suspend fun claimStakedPayout(email: String): Boolean {
        val user = dao.getUserByEmail(email) ?: return false
        if (user.stakedBalance <= 0) return false

        val now = System.currentTimeMillis()
        val elapsedMs = if (user.lastStakeTimestamp > 0) now - user.lastStakeTimestamp else 0L
        
        val yearMs = 31536000000.0
        val interestEarned = user.stakedBalance * (elapsedMs.toDouble() / yearMs) * 0.18
        
        val principal = user.stakedBalance
        val returnedBalance = principal + interestEarned
        
        val updatedUser = user.copy(
            mainBalance = user.mainBalance + returnedBalance,
            stakedBalance = 0.0,
            lastStakeTimestamp = 0L
        )
        dao.updateUser(updatedUser)
        return true
    }

    suspend fun simulateSwapTokens(email: String, mgtAmount: Double): Boolean {
        val user = dao.getUserByEmail(email) ?: return false
        if (user.mainBalance < mgtAmount || mgtAmount <= 0) return false
        
        val updatedUser = user.copy(
            mainBalance = user.mainBalance - mgtAmount
        )
        dao.updateUser(updatedUser)
        return true
    }

    suspend fun incrementUserBalance(email: String, amount: Double): Boolean {
        val user = dao.getUserByEmail(email) ?: return false
        val updatedUser = user.copy(
            mainBalance = user.mainBalance + amount,
            totalMinedLifetime = user.totalMinedLifetime + amount
        )
        dao.updateUser(updatedUser)
        return true
    }
}
