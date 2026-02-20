package com.example.adegavirtual.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BeverageDao {
    @Query("SELECT * FROM beverages ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<Beverage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Beverage): Long

    @Update
    suspend fun update(item: Beverage)

    @Delete
    suspend fun delete(item: Beverage)

    @Query("UPDATE beverages SET stock = stock - 1 WHERE id = :id AND stock > 0")
    suspend fun decrementStock(id: Long)
}
