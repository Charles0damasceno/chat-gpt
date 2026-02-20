package com.example.adegavirtual.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Beverage::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun beverageDao(): BeverageDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "adega_virtual.db"
                ).build().also { instance = it }
            }
        }
    }
}
