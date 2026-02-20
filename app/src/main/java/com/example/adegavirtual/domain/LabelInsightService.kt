package com.example.adegavirtual.domain

interface LabelInsightService {
    suspend fun extractInfo(labelText: String): BeverageInfo
}

class FakeLabelInsightService : LabelInsightService {
    override suspend fun extractInfo(labelText: String): BeverageInfo {
        val normalized = labelText.ifBlank { "Rótulo não reconhecido" }
        return BeverageInfo(
            name = normalized.lineSequence().firstOrNull()?.take(40) ?: "Bebida desconhecida",
            brand = "Marca estimada por OCR",
            type = if (normalized.contains("vinho", ignoreCase = true)) "Vinho" else "Destilado / Outro",
            description = "Descrição gerada localmente. Integre com API de IA para dados oficiais da bebida.",
            curiosities = "Curiosidade: leia safra, origem e teor alcoólico no rótulo para recomendações melhores.",
            drinkTips = "Sugestão: use a bebida em coquetéis clássicos e ajuste o dulçor com xaropes cítricos.",
            pairingTips = "Harmonização: combine com queijos, carnes grelhadas ou sobremesas conforme doçura e corpo."
        )
    }
}
