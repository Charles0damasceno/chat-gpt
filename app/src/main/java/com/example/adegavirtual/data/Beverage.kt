package com.example.adegavirtual.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beverages")
data class Beverage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val brand: String,
    val type: String,
    val description: String,
    val curiosities: String,
    val drinkTips: String,
    val pairingTips: String,
    val stock: Int,
    val recognizedLabelText: String,
    val createdAt: Long = System.currentTimeMillis()
)
