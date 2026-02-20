package com.example.adegavirtual.domain

import com.example.adegavirtual.data.Beverage
import com.example.adegavirtual.data.BeverageDao
import kotlinx.coroutines.flow.Flow

class BeverageRepository(
    private val beverageDao: BeverageDao,
    private val labelInsightService: LabelInsightService
) {
    fun observeInventory(): Flow<List<Beverage>> = beverageDao.observeAll()

    suspend fun registerFromLabelText(labelText: String, stock: Int) {
        val info = labelInsightService.extractInfo(labelText)
        beverageDao.insert(
            Beverage(
                name = info.name,
                brand = info.brand,
                type = info.type,
                description = info.description,
                curiosities = info.curiosities,
                drinkTips = info.drinkTips,
                pairingTips = info.pairingTips,
                stock = stock,
                recognizedLabelText = labelText
            )
        )
    }

    suspend fun consumeOneBottle(id: Long) = beverageDao.decrementStock(id)
}
