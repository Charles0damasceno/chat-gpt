package com.example.adegavirtual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.adegavirtual.data.AppDatabase
import com.example.adegavirtual.domain.BeverageRepository
import com.example.adegavirtual.domain.FakeLabelInsightService
import com.example.adegavirtual.ui.AdegaScreen
import com.example.adegavirtual.ui.AdegaViewModel
import com.example.adegavirtual.ui.AdegaViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: AdegaViewModel by viewModels {
        val dao = AppDatabase.get(this).beverageDao()
        val repository = BeverageRepository(dao, FakeLabelInsightService())
        AdegaViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdegaScreen(viewModel = viewModel)
        }
    }
}
