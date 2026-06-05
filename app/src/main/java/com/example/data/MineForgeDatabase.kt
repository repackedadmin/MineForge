package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UserAccount::class, MiningRig::class, WithdrawalRequest::class],
    version = 2,
    exportSchema = false
)
abstract class MineForgeDatabase : RoomDatabase() {

    abstract fun mineForgeDao(): MineForgeDao

    companion object {
        @Volatile
        private var INSTANCE: MineForgeDatabase? = null

        fun getDatabase(context: Context): MineForgeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MineForgeDatabase::class.java,
                    "mineforge_database"
                )
                .fallbackToDestructiveMigration() // Simple for development
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
