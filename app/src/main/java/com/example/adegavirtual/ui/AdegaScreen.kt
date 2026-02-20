package com.example.adegavirtual.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.adegavirtual.data.Beverage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AdegaScreen(
    viewModel: AdegaViewModel,
    ocrLabelReader: OcrLabelReader = OcrLabelReader()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val inventory by viewModel.inventory.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var stockInput by remember { mutableIntStateOf(1) }
    var recognizedText by remember { mutableStateOf("") }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch(Dispatchers.IO) {
            val text = runCatching { ocrLabelReader.readLabel(context, uri) }
                .getOrElse { "Falha no OCR: ${it.message}" }
            recognizedText = text
        }
    }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Adega Virtual", style = MaterialTheme.typography.headlineSmall)
            Text("1) Tire foto do rótulo  2) Gere informações  3) Controle estoque")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { imagePicker.launch("image/*") }) {
                    Text("Escolher foto")
                }
                Button(onClick = { viewModel.registerFromLabel(recognizedText, stockInput) }) {
                    Text("Adicionar na adega")
                }
            }

            OutlinedTextField(
                value = stockInput.toString(),
                onValueChange = { stockInput = it.toIntOrNull()?.coerceAtLeast(1) ?: 1 },
                label = { Text("Quantidade inicial") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = recognizedText,
                onValueChange = { recognizedText = it },
                label = { Text("Texto do rótulo (OCR)") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Meu estoque", style = MaterialTheme.typography.titleMedium)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(inventory) { item ->
                    BeverageCard(item = item, onConsume = { viewModel.consumeBottle(item) })
                }
            }
        }
    }
}

@Composable
private fun BeverageCard(item: Beverage, onConsume: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("${item.name} (${item.type})", style = MaterialTheme.typography.titleMedium)
            Text("Marca: ${item.brand}")
            Text("Descrição: ${item.description}")
            Text("Curiosidades: ${item.curiosities}")
            Text("Drinks: ${item.drinkTips}")
            Text("Acompanhamentos: ${item.pairingTips}")
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Estoque: ${item.stock}")
                Button(onClick = onConsume, enabled = item.stock > 0) {
                    Text("Dar baixa")
                }
            }
        }
    }
}
