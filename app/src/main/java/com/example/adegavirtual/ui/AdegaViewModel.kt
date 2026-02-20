package com.example.adegavirtual.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.adegavirtual.data.Beverage
import com.example.adegavirtual.domain.BeverageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdegaViewModel(
    private val repository: BeverageRepository
) : ViewModel() {
    val inventory = repository.observeInventory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun registerFromLabel(labelText: String, stock: Int) {
        viewModelScope.launch {
            runCatching {
                repository.registerFromLabelText(labelText, stock)
            }.onSuccess {
                _message.value = "Bebida adicionada à adega com sucesso."
            }.onFailure {
                _message.value = "Falha ao processar rótulo: ${it.message}"
            }
        }
    }

    fun consumeBottle(item: Beverage) {
        viewModelScope.launch {
            repository.consumeOneBottle(item.id)
            _message.value = if (item.stock > 0) {
                "1 garrafa consumida de ${item.name}."
            } else {
                "Estoque já está zerado para ${item.name}."
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}

class AdegaViewModelFactory(
    private val repository: BeverageRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdegaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdegaViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel não suportado")
    }
}
