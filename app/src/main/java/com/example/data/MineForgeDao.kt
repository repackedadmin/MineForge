package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MineForgeDao {

    // --- User Queries ---
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserAccount?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun observeUserByEmail(email: String): Flow<UserAccount?>

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    suspend fun getActiveUser(): UserAccount?

    @Query("SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1")
    fun observeActiveUser(): Flow<UserAccount?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserAccount)

    @Update
    suspend fun updateUser(user: UserAccount)

    @Query("UPDATE users SET isLoggedIn = 0")
    suspend fun logoutAllUsers()

    @Query("DELETE FROM users WHERE email = :email")
    suspend fun deleteUserByEmail(email: String)


    // --- Mining Rig Queries ---
    @Query("SELECT * FROM mining_rigs WHERE userEmail = :userEmail ORDER BY purchaseTimestamp DESC")
    fun observeRigsForUser(userEmail: String): Flow<List<MiningRig>>

    @Query("SELECT * FROM mining_rigs WHERE userEmail = :userEmail")
    suspend fun getRigsForUser(userEmail: String): List<MiningRig>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRig(rig: MiningRig)

    @Update
    suspend fun updateRig(rig: MiningRig)

    @Query("DELETE FROM mining_rigs WHERE userEmail = :userEmail")
    suspend fun deleteRigsForUser(userEmail: String)


    // --- Withdrawal Queries ---
    @Query("SELECT * FROM withdrawals WHERE userEmail = :userEmail ORDER BY createdAt DESC")
    fun observeWithdrawalsForUser(userEmail: String): Flow<List<WithdrawalRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWithdrawal(withdrawal: WithdrawalRequest)

    @Query("DELETE FROM withdrawals WHERE userEmail = :userEmail")
    suspend fun deleteWithdrawalsForUser(userEmail: String)
}
