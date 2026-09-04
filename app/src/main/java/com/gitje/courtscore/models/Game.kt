package com.gitje.courtscore.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.gitje.courtscore.R
import java.time.LocalDateTime
import java.util.UUID

data class Game(
    val date: LocalDateTime,
    val scoreHistory: List<ScoreEvent>,
    val winner: PlayerId,// sent from watch
    val sport: Sports
) {
    val id: UUID = UUID.randomUUID()

    @Composable
    fun getIcon(): ImageVector {
        return ImageVector.vectorResource(
            when (sport) {
                Sports.Tennis -> R.drawable.ic_tennis
                Sports.Padel -> R.drawable.ic_padel
                else -> R.drawable.ic_badminton
            }
        )
    }
}