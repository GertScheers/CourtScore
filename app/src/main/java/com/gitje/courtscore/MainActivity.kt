package com.gitje.courtscore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.gitje.courtscore.presentation.composables.Overview
import com.gitje.courtscore.ui.theme.CourtScoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CourtScoreTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Overview(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}